package kg.automoika.extensions

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.get
import java.util.*
import kotlin.random.Random


fun generateId(): String {
    return Random.nextInt(0, 900000).toString()
}

fun generateToken(): String {
    val newToken = UUID.randomUUID().toString()
    return newToken.replace("-", "")
}

fun createHttpClient(): HttpClient {
    return HttpClient(CIO) {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.HEADERS
        }
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        followRedirects = false
    }
}

suspend fun HttpClient.submitFormWithBinaryData(url: String, bytes: ByteArray, name: String): HttpResponse {
    return submitFormWithBinaryData(
        url = url,
        formData = formData {
            append("image", bytes, Headers.build {
                append(HttpHeaders.ContentType, "image/png")
                append(HttpHeaders.ContentDisposition, "filename=\"$name\"")
            })
        }
    )
}

fun String.hasText(text : String): Boolean {
    return contains(text, true)
}

fun daysBetween(dateOne: Long, dateTwo: Long): Int {
    var days = 0
    for (i in dateOne..dateTwo step 86400000) days++
    return days
}