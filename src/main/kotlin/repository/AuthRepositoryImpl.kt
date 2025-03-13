package kg.automoika.repository

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kg.automoika.data.response.TokenResponse
import kg.automoika.db.AuthDatabase
import kg.automoika.extensions.daysBetween

class AuthRepositoryImpl(private val database: AuthDatabase) : AuthRepository {

    override suspend fun checkAuth(call: ApplicationCall): Boolean {
        val authHeader = call.request.header("Authorization")
        val hasToken = if (authHeader.isNullOrEmpty() || authHeader.length < 15) false
        else {
            val authToken = authHeader.replace("Token", "").replace(" ", "")
            database.getToken(authToken) != null
        }

        //if (!hasToken) call.respond(HttpStatusCode.Unauthorized)
        //return hasToken
        return true
    }

    override suspend fun checkToken(id: String): Boolean {
        val token = database.getTokenById(id)
        return if (token == null) false
        else daysBetween(token.createdAt.toLong(), System.currentTimeMillis()) < 30
    }

    override suspend fun createToken(): TokenResponse? {
        return database.createToken()
    }
}