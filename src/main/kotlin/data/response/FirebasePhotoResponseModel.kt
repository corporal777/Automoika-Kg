package kg.automoika.data.response

import kotlinx.serialization.Serializable

@Serializable
data class FirebasePhotoResponseModel(
    val name : String,
    val bucket : String,
    val downloadTokens : String,
)