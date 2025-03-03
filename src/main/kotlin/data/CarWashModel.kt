package kg.automoika.data

import kg.automoika.extensions.generateId
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class CarWashModel(
    val name: String,
    val street: String,
    val city: String,
    val boxesCount: Int
) {

    fun toRemote(): CarWashRemote {
        val boxes = arrayListOf<CarWashBoxes>()
        for (i in 0 until boxesCount) {
            val boxNum = i + 1
            boxes.add(CarWashBoxes(generateId(), boxNum.toString(), true))
        }
        return CarWashRemote(
            generateId(),
            name,
            CarWashAddress(street, city),
            boxes
        )
    }
}