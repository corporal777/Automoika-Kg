package kg.automoika.extensions

import com.google.firebase.cloud.StorageClient
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.content.*
import kg.automoika.data.response.FirebasePhotoResponseModel
import java.io.File

object FileUtils {

    fun PartData.FileItem.saveImageLocal(path: String): String? {
        try {
            val fileBytes = streamProvider().readBytes()
            val fileExtension = originalFileName?.takeLastWhile { it != '.' }
            val fileName = originalFileName ?: ("image" + generateId() + (".$fileExtension"))

            val folder = File(path)
            if (!folder.parentFile.exists()) folder.parentFile.mkdirs()
            folder.mkdir()
            File("${path}/$fileName").writeBytes(fileBytes)
            return fileName
        } catch (e : Exception){
            return null
        }
    }

    fun deleteLocalFile(path: String){
        File(path).delete()
    }

    suspend fun loadImagesToFirebase(client: HttpClient, bytes: ByteArray, noteId: String, index: Int): String {
        //val fileName = "($noteId)-$index"
        val fileName = noteId
        val url = "https://firebasestorage.googleapis.com/v0/b/tam-tam-8b2a7.appspot.com/o/$fileName"
        val response: HttpResponse = client.submitFormWithBinaryData(url, bytes, fileName)

        var imageUrl = ""
        if (response.status == HttpStatusCode.OK) {
            val json = response.body<FirebasePhotoResponseModel>()

            val token = json.downloadTokens
            imageUrl = URLBuilder(url).apply {
                set {
                    parameters.append("alt", "media")
                    parameters.append("token", token)
                }
            }.buildString()
        }
        return imageUrl
    }

    suspend fun deleteImagesFromFirebase(imgName : String): Boolean {
        return StorageClient.getInstance().bucket("tam-tam-8b2a7.appspot.com")
            .get(imgName).delete()
    }
}