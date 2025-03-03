package kg.automoika

import io.ktor.server.application.*
import io.ktor.server.routing.*
import kg.automoika.data.CarWashTable
import kg.automoika.routes.carWashRoutes
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureRouting() {
    routing {
        carWashRoutes()
    }
}

fun Application.configureDatabases() {
    val db = Database.connect(
        "jdbc:postgresql://localhost:5432/automoika-db?currentSchema=car-washes",
        user = "postgres",
        password = "12345",
        driver = "org.postgresql.Driver"
    )
    transaction(db) {
        SchemaUtils.create(CarWashTable)
    }
}
