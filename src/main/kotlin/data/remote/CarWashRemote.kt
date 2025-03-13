package kg.automoika.data.remote

import kg.automoika.data.body.CarWashBody
import kg.automoika.data.response.CarWashShortResponse
import kg.automoika.extensions.generateId
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId

@Serializable
data class CarWashRemote(
    @BsonId
    val id: String,
    val name: String,
    val description: String,
    val createdAt: String,
    val backgroundImage: CarWashImageModel,
    val images: List<CarWashImageModel>,
    val address: CarWashLocationModel,
    val contacts : CarWashContactsModel,
    val boxesCount: String,
    var freeBoxes : List<String>,
    var inFavorite : List<String>,
    val cleaningType : String
){
    companion object {
        fun fromBody(body: CarWashBody, imagesList: List<CarWashImageModel>): CarWashRemote {
            return CarWashRemote(
                id = generateId(),
                name = body.name,
                description = body.description,
                createdAt = System.currentTimeMillis().toString(),
                backgroundImage = body.backgroundImage ?: CarWashImageModel("",""),
                images = imagesList,
                address = CarWashLocationModel(body.street, body.city, body.lat, body.lon, body.wayDescription),

                boxesCount = body.boxesCount,
                freeBoxes = listOf("0"),
                contacts = CarWashContactsModel(body.phone, body.whatsapp, body.telegram, body.instagram),

                inFavorite = listOf(""),
                cleaningType = body.cleaningType
            )
        }
    }

    fun toResponse(): CarWashShortResponse {
        return CarWashShortResponse(
            id = id,
            name = name,
            backgroundImage = backgroundImage.imageUrl,
            address = address,
            boxesCount = boxesCount,
            freeBoxes = freeBoxes,
            inFavorite = inFavorite
        )
    }
}

@Serializable
data class CarWashLocationModel(
    val street : String,
    val city : String,
    val lat : String,
    val lon : String,
    val wayDescription : String
)

@Serializable
data class CarWashImageModel(
    val imageName : String,
    val imageUrl : String
)

@Serializable
data class CarWashContactsModel(
    val phone : String,
    val whatsapp : String,
    val telegram : String,
    val instagram : String
)



