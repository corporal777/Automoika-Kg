package kg.automoika.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kg.automoika.cache.AppData
import kg.automoika.repository.AuthRepository
import kg.automoika.repository.CarWashRepository
import kg.automoika.repository.CommonRepository
import org.koin.ktor.ext.inject

fun Route.databaseRoutes() {
    val repository by inject<CommonRepository>()

    post("v1/copy-data") {
        val list = repository.copyToLocalDbFromRemote()
        if (list.isEmpty()) call.respond(HttpStatusCode.Forbidden)
        else call.respond(HttpStatusCode.OK, list)
    }
}