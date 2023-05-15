package util

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import org.jetbrains.skia.Image

class ImageDownloader {
    private val downloadClient = HttpClient(CIO)

    suspend fun downloadImage(url: String): ImageBitmap {
        val image = downloadClient.get(url).readBytes()
        return Image.makeFromEncoded(image).toComposeImageBitmap()
    }
}