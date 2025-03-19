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
    val middleName: String,
    val createdAt: String,
    val image: CarWashImageModel,
    val login : UserContactsModel
) {
    fun toResponse() : UserResponse = UserResponse(id, name, lastName, middleName, image, login)

    companion object {
        fun create(body: UserBody): UserRemote {
            return UserRemote(
                id = body.id,
                name = body.name,
                lastName = body.lastName,
                middleName = body.middleName,
                createdAt = System.currentTimeMillis().toString(),
                image = body.image ?: CarWashImageModel("",""),
                login = UserContactsModel(body.phone, true)
            )
        }
    }

}

@Serializable
data class UserContactsModel(
    val phone : String,
    val isConfirmed : Boolean,
)