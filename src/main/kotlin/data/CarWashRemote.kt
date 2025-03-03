package kg.automoika.data

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class CarWashRemote(
    val id: String,
    val name: String,
    val address: CarWashAddress,
    val boxes: List<CarWashBoxes>
)

@Serializable
data class CarWashAddress(
    val street : String,
    val city : String
)

@Serializable
data class CarWashBoxes(
    val id : String,
    val number : String,
    val isFree : Boolean
)