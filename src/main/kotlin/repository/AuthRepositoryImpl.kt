package kg.automoika.repository

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import io.ktor.server.application.*
import io.ktor.server.request.*
import kg.automoika.data.body.CodeBody
import kg.automoika.data.body.LoginBody
import kg.automoika.data.body.PhoneBody
import kg.automoika.data.remote.AccountRemote
import kg.automoika.data.remote.UserRemote
import kg.automoika.data.response.TokenResponse
import kg.automoika.db.AuthDatabase
import kg.automoika.extensions.ACCOUNTS_COLLECTION
import kg.automoika.extensions.NotificationUtils
import kg.automoika.extensions.USERS_COLLECTION
import kg.automoika.extensions.daysBetween
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList

class AuthRepositoryImpl(
    private val localDb: AuthDatabase,
    private val remoteDb: MongoDatabase
) : AuthRepository {

    private val codesMap = mutableMapOf<String, String>()

    override suspend fun checkAuth(call: ApplicationCall): Boolean {
        val authHeader = call.request.header("Authorization")
        val hasToken = if (authHeader.isNullOrEmpty() || authHeader.length < 15) false
        else localDb.getToken(authHeader.replace("Token", "").replace(" ", "")) != null
        //if (!hasToken) call.respond(HttpStatusCode.Unauthorized)
        //return hasToken
        return true
    }

    override suspend fun checkToken(token: String): Boolean {
        val local = localDb.getToken(token)
        return if (local == null) false
        else daysBetween(local.createdAt.toLong(), System.currentTimeMillis()) < 30
    }

    override suspend fun createToken(): TokenResponse? {
        return localDb.createToken()
    }


    override suspend fun login(body: LoginBody): AccountRemote? {
        val account = remoteDb.getCollection<AccountRemote>(ACCOUNTS_COLLECTION)
            .find(Filters.eq("login", body.login)).firstOrNull()
        return account
    }

    override suspend fun sendCode(body: PhoneBody): Boolean {
        val code = localDb.createCode(body.phone)
        val result = NotificationUtils.sendVerificationCode(body.fcmToken, code)
        return !result.isNullOrEmpty()
    }

    override suspend fun confirmCode(body: CodeBody): Boolean {
        return localDb.checkCode(body.code, body.phone)
    }


    override suspend fun checkPhoneExists(phone: String): String? {
        val users = remoteDb.getCollection<UserRemote>(USERS_COLLECTION).find().toList()
        return users.find { x -> x.login.phone == phone }?.id
    }
}