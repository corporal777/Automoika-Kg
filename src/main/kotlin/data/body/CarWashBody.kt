package kg.automoika.data.body

import io.ktor.http.content.*
import kg.automoika.data.remote.CarWashImageModel
import kg.automoika.extensions.FileUtils.uploadImageToFirebase
import kg.automoika.extensions.generateId
import kg.automoika.extensions.generateShortId

data class CarWashBody(
    val id : String,
    var name: String = "",
    var description: String = "",

    var city: String = "",
    var street: String = "",
    var lat: String = "",
    var lon: String = "",
    var wayDescription : String = "",

    var backgroundImage: CarWashImageModel? = null,
    var boxesCount: String = "0",

    var phone : String = "",
    var whatsapp : String = "",
    var telegram : String = "",
    var instagram : String = "",


    var type : String = ""
    ) {
    companion object {
        fun CarWashBody.setData(partData: PartData.FormItem) {
            when (partData.name) {
                "name" -> name = partData.value
                "description" -> description = partData.value

                "city" -> city = partData.value
                "street" -> street = partData.value
                "lat" -> lat = partData.value
                "lon" -> lon = partData.value
                "wayDescription" -> wayDescription = partData.value

                "boxes" -> boxesCount = partData.value

                "phone" -> phone = partData.value
                "whatsapp" -> whatsapp = partData.value
                "telegram" -> telegram = partData.value
                "instagram" -> instagram = partData.value

                "type" -> type = partData.value
            }
        }

        fun CarWashBody.setBackgroundImage(fileName: String, fileUrl : String) {
            backgroundImage = CarWashImageModel(fileName, fileUrl)
        }
    }
}

