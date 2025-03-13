package kg.automoika.data.response

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val id : String,
    val token: String,
    val createdAt : String
)