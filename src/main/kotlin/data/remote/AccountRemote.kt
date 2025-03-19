package kg.automoika.data.remote

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId

@Serializable
data class AccountRemote(
    @BsonId
    val id: String,
    val accountType : String,
    val login : String
){
    companion object {
        fun setCarWashAccount(id: String, login : String): AccountRemote {
            return AccountRemote(id, "car-wash", login)
        }

        fun setUserAccount(id: String, login : String): AccountRemote {
            return AccountRemote(id, "user", login)
        }
    }
}
