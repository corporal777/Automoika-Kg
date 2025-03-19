package kg.automoika

import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.tomcat.*
import kg.automoika.di.dataBaseModule
import kg.automoika.di.mongoModule
import kg.automoika.di.repositoryModule
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>): Unit {
    EngineMain.main(args)
}

fun Application.module() {
    install(ContentNegotiation) {
        gson {}
    }
    install(Koin) {
        modules(mongoModule(getMongoUri(), getMongoDb()), dataBaseModule, repositoryModule,)
    }
    configureDatabases(environment.config)
    configureFirebase(environment.config)
    configureSockets()

    configureRouting()
}

private fun Application.getMongoUri(): String {
    return environment.config.propertyOrNull("ktor.mongo.uri")?.getString()
        ?: throw RuntimeException("Failed to access MongoDataBase URI.")
}

private fun Application.getMongoDb(): String {
    return environment.config.propertyOrNull("ktor.mongo.database")?.getString()
        ?: throw RuntimeException("Failed to access MongoDataBase")
}