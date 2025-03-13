package kg.automoika.data.body

import io.ktor.http.content.*
import kg.automoika.data.remote.CarWashImageModel
import kg.automoika.extensions.*
import kg.automoika.extensions.FileUtils.saveImageLocal
import kotlinx.serialization.Serializable

@Serializable
data class UserBody(
    var name: String = "",
    var lastName: String = "",
    var city: String = "",
    var street: String = "",
    var lat: String = "",
    var lon: String = "",
    var image: CarWashImageModel? = null,
    var phone : String = "",
    var whatsapp : String = "",
){

    fun setData(partData: PartData.FormItem) {
        when (partData.name) {
            "name" -> name = partData.value
            "lastName" -> lastName = partData.value

            "city" -> city = partData.value
            "street" -> street = partData.value

            "lat" -> lat = partData.value
            "lon" -> lon = partData.value

            "phone" -> phone = partData.value
            "whatsapp" -> whatsapp = partData.value
        }
    }

    fun setImage(partData: PartData.FileItem) {
        val fileName = partData.saveImageLocal(USERS_LOCAL_IMAGE_FULL_PATH) ?: return
        val fileUrl = "$EXTERNAL_IMAGE_PATH/$fileName"
        image = CarWashImageModel(fileName, fileUrl)
    }
}