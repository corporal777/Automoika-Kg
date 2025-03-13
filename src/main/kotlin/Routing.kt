package kg.automoika

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.serialization.kotlinx.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.utils.io.*
import kg.automoika.db.CarWashBoxesTable
import kg.automoika.db.CarWashTable
import kg.automoika.db.TokenTable
import kg.automoika.extensions.*
import kg.automoika.routes.authRoutes
import kg.automoika.routes.carWashRoutes
import kg.automoika.routes.socketRoutes
import kg.automoika.routes.userRoutes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.io.FileInputStream
import java.time.Duration
import kotlinx.coroutines.*

fun Application.configureRouting() {
    routing {
        carWashRoutes()
        authRoutes()
        socketRoutes()
        userRoutes()
        configureStaticFolders()
    }
}

fun Routing.configureStaticFolders(){
    static("/") {
        staticRootFolder = File(STATIC_LOCAL_FILES_FOLDER)
        static(EXTERNAL_POINT_IMAGE_PATH){
            files(POINTS_LOCAL_IMAGES_DIRECTORY)
            files(USERS_LOCAL_IMAGES_DIRECTORY)
        }
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
        SchemaUtils.create(CarWashTable, TokenTable)
    }
}

fun Application.configureFirebase() {
    val fileContent = javaClass.getResource("tam-tam-8b2a7-firebase-adminsdk-t7lnx-74ddd78486.json")?.readText()
    if (fileContent.isNullOrEmpty()) return
    val options = FirebaseOptions.Builder()
        .setCredentials(GoogleCredentials.fromStream(FileInputStream(fileContent)))
        .setServiceAccountId("firebase-adminsdk-t7lnx@tam-tam-8b2a7.iam.gserviceaccount.com")
        .setDatabaseUrl("https://tam-tam-8b2a7-default-rtdb.firebaseio.com")
        .build()
    FirebaseApp.initializeApp(options)
}


fun Application.configureSockets() {
    install(WebSockets) {
        contentConverter = KotlinxWebsocketSerializationConverter(Json)
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
}