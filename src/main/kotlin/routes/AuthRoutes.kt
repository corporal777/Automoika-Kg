package kg.automoika.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kg.automoika.cache.AppData
import kg.automoika.repository.AuthRepository
import kg.automoika.repository.CarWashRepository
import kotlinx.coroutines.flow.collectLatest
import org.koin.ktor.ext.inject

fun Route.authRoutes() {
    val repository by inject<AuthRepository>()

    get("v1/get-token") {
        val token = repository.createToken()
        if (token == null) call.respond(HttpStatusCode.Conflict)
        else call.respond(HttpStatusCode.Created, token)
    }

    get("v1/check-token/{id}") {
        val id = call.parameters["id"]
        if (id.isNullOrEmpty()) call.respond(HttpStatusCode.BadRequest)
        else {
            val isNotExpired = repository.checkToken(id)
            if (isNotExpired) call.respond(HttpStatusCode.OK)
            else call.respond(HttpStatusCode.Forbidden)
        }
    }
}