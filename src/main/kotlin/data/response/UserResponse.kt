package kg.automoika.data.response

import kg.automoika.data.remote.CarWashImageModel
import kg.automoika.data.remote.UserContactsModel
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: String,
    val name: String,
    val lastName: String,
    val middleName: String,
    val image: CarWashImageModel,
    val login : UserContactsModel,
)