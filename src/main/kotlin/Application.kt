package kg.automoika

import com.mongodb.kotlin.client.coroutine.MongoClient
import io.ktor.serialization.gson.gson
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.tomcat.EngineMain
import kg.automoika.cache.AppData
import kg.automoika.db.AuthDatabase
import kg.automoika.db.CarWashDatabase
import kg.automoika.di.dataBaseModule
import kg.automoika.di.mongoModule
import kg.automoika.di.repositoryModule
import kg.automoika.repository.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun main(args: Array<String>): Unit {
    EngineMain.main(args)
}

fun Application.module() {
    install(ContentNegotiation) {
        gson {}
    }
    install(Koin) {
        slf4jLogger()
        modules(mongoModule(getMongoUri(), getMongoDb()), dataBaseModule, repositoryModule,)
    }
    configureDatabases()
    configureFirebase()
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