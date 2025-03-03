package kg.automoika.data

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class CarWashRemote(
    val id: String,
    val name: String,
    val address: CarWashAddress,
    val boxes: List<CarWashBoxes>
)

data class CarWashAddress(
    val street : String,
    val city : String
)

data class CarWashBoxes(
    val id : String,
    val number : String,
    val isFree : Boolean
)