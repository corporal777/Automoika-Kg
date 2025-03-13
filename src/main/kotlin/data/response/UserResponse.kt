package kg.automoika.data.response

import kg.automoika.data.remote.CarWashImageModel
import kg.automoika.data.remote.UserContactsModel
import kg.automoika.data.remote.UserLocationModel
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId

@Serializable
data class UserResponse(
    val id: String,
    val name: String,
    val lastName: String,
    val image: CarWashImageModel,
    val address: UserLocationModel,
    val contacts : UserContactsModel,
)