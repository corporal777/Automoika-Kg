package kg.automoika.routes

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kg.automoika.data.CarWashModel
import kg.automoika.repository.CarWashRepository
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.koin.ktor.ext.inject
import java.lang.reflect.Constructor

fun Route.carWashRoutes() {
    val repository by inject<CarWashRepository>()

    get("v1/notes") {}

    post("v1/create-car-wash") {
        val request = call.receive<CarWashModel>()
        val noteResponse = repository.createCarWash(request)
        if (noteResponse != null) call.respond(HttpStatusCode.OK)
        else call.respond(HttpStatusCode.Conflict)
    }

    post("v1/add-task"){
        try {
            val result = repository.addTask()
            result?.let {
                call.respond(HttpStatusCode.OK)
            } ?: call.respond(HttpStatusCode.NotImplemented,"Error adding car wash")
        }catch (e: ExposedSQLException){
            call.respond(HttpStatusCode.BadRequest,e.message ?: "SQL Exception!!")
        }
    }


}

