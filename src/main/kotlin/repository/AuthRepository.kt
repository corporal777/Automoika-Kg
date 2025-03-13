package kg.automoika.repository

import io.ktor.server.application.*
import kg.automoika.data.response.TokenResponse

interface AuthRepository {

    suspend fun checkAuth(call: ApplicationCall) : Boolean
    suspend fun createToken() : TokenResponse?
    suspend fun checkToken(id : String) : Boolean
}