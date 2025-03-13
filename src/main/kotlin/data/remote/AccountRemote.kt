package kg.automoika.data.remote

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId

@Serializable
data class AccountRemote(
    @BsonId
    val id: String,
    val accountType : String,
    val payment : String
){
    companion object {
        fun setCarWashAccount(id: String): AccountRemote {
            return AccountRemote(id, "car-wash-owner", "absent")
        }
    }
}
