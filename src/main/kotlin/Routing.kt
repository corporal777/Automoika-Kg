package kg.automoika

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kg.automoika.data.routes.carWashRoutes

fun Application.configureRouting() {
    routing {
        carWashRoutes()
    }
}
