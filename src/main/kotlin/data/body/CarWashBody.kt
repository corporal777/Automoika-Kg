package kg.automoika.data.body

import io.ktor.http.content.*
import kg.automoika.data.remote.CarWashImageModel

data class CarWashBody(
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

    var cleaningType : String = ""

    ) {
    companion object {
        fun CarWashBody.setData(partData: PartData.FormItem) {
            when (partData.name) {
                "name" -> name = partData.value
                "description" -> description = partData.value

                "boxes" -> boxesCount = partData.value

                "phone" -> phone = partData.value
                "whatsapp" -> whatsapp = partData.value
                "telegram" -> telegram = partData.value
                "instagram" -> instagram = partData.value

                "city" -> city = partData.value
                "street" -> street = partData.value
                "lat" -> lat = partData.value
                "lon" -> lon = partData.value
                "wayDescription" -> wayDescription = partData.value

                "cleaningType" -> cleaningType = partData.value
            }
        }

        fun CarWashBody.setBackgroundImage(fileName: String, fileUrl : String) {
            backgroundImage = CarWashImageModel(fileName, fileUrl)
        }
    }
}

