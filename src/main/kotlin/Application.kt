package kg.automoika

import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import io.ktor.serialization.gson.gson
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.routing.routing
import io.ktor.server.tomcat.EngineMain
import kg.automoika.repository.CarWashRepository
import kg.automoika.repository.CarWashRepositoryImpl
import org.koin.core.scope.Scope
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    install(ContentNegotiation) {
        gson {}
    }
    install(Koin) {
        slf4jLogger()
        modules(
            module {
                single { MongoClient.create(getMongoUriProperty()) }
                single { getMongoDatabaseProperty(this) }
            },
            module {
                single<CarWashRepository> { CarWashRepositoryImpl(get()) }
            })
    }
    configureRouting()
}

private fun Application.getMongoUriProperty(): String {
    val url = "mongodb+srv://flay:3tnXuYZbkHL0bGH2@clustercarwash.fn4om.mongodb.net/?retryWrites=true&w=majority&appName=ClusterCarWash"
//    return environment.config.propertyOrNull(url)?.getString()
//        ?: throw RuntimeException("Failed to access MongoDB URI.")
    return url
}

private fun Application.getMongoDatabaseProperty(scope: Scope): MongoDatabase {
    return scope.get<MongoClient>().getDatabase("CarWashDatabase")
}