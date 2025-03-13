package kg.automoika.data.response

import kg.automoika.data.remote.CarWashLocationModel
import kotlinx.serialization.Serializable

@Serializable
data class CarWashShortResponse(
    val id: String,
    val name: String,
    val backgroundImage: String,
    val address : CarWashLocationModel,
    val boxesCount: String,
    var freeBoxes : List<String>,
    val inFavorite : List<String>
)