package kg.automoika.data.remote

import kg.automoika.data.body.CarWashBody
import kg.automoika.data.body.UserBody
import kg.automoika.data.response.UserResponse
import kg.automoika.extensions.generateId
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId

@Serializable
data class UserRemote(
    @BsonId
    val id: String,
    val name: String,
    val lastName: String,
    val createdAt: String,
    val image: CarWashImageModel,
    val address: UserLocationModel,
    val contacts : UserContactsModel,
) {
    fun toResponse() : UserResponse = UserResponse(id, name, lastName, image, address, contacts)

    companion object {
        fun fromBody(body: UserBody ): UserRemote {
            return UserRemote(
                id = generateId(),
                name = body.name,
                lastName = body.lastName,
                createdAt = System.currentTimeMillis().toString(),
                image = body.image ?: CarWashImageModel("",""),
                address = UserLocationModel(body.street, body.city, body.lat, body.lon),
                contacts = UserContactsModel(body.phone, body.whatsapp, false),
            )
        }
    }

}

@Serializable
data class UserContactsModel(
    val phone : String,
    val whatsapp : String,
    val showInProfile : Boolean
)


@Serializable
data class UserLocationModel(
    val street : String,
    val city : String,
    val lat : String,
    val lon : String,
)