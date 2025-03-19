package kg.automoika.data.body

import io.ktor.http.content.*
import kg.automoika.data.remote.CarWashImageModel
import kg.automoika.extensions.*
import kg.automoika.extensions.FileUtils.saveImageLocal
import kg.automoika.extensions.FileUtils.uploadImageToFirebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

@Serializable
data class UserBody(
    val id : String,
    var name: String = "",
    var lastName: String = "",
    var middleName: String = "",
    var phone : String = "",
    var image: CarWashImageModel? = null,
){

    fun setData(partData: PartData.FormItem) {
        when (partData.name) {
            "name" -> name = partData.value
            "lastName" -> lastName = partData.value
            "middleName" -> middleName = partData.value
            "phone" -> phone = partData.value
        }
    }

    suspend fun setImage(partData: PartData.FileItem) {
        //val fileName = partData.saveImageLocal(USERS_LOCAL_IMAGE_FULL_PATH) ?: return
        //val fileUrl = "$EXTERNAL_IMAGE_PATH/$fileName"

        val fileBytes = partData.streamProvider().readBytes()
        val fileName = "user-$id"
        val fileUrl = uploadImageToFirebase(fileBytes, fileName)
        image = CarWashImageModel(fileName, fileUrl)
    }
}