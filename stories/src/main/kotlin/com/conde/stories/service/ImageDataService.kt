package com.conde.stories.service

import com.conde.stories.infrastructure.util.createUUID
import com.conde.stories.infrastructure.util.useInBackground
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileNotFoundException

@Service
class ImageDataService {

    private val photoDirectory = File("storiesPhotos")
    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        println("storiesPhotos directory is directory: ${photoDirectory.isDirectory}")
        if (!photoDirectory.isDirectory) {
            photoDirectory.delete().also {
                println("storiesPhotos file deleted: $it")
            }
            photoDirectory.mkdir().also {
                println("storiesPhotos directory created: $it")
            }
        }
    }

    fun saveAsNewJpegImage(imageName: String, photo: ByteArray) : String {
        val jpegName = "$imageName-${createUUID()}.jpeg"
        scope.launch {
            saveFile(jpegName, photo)
        }
        return jpegName
    }

    suspend fun saveFile(fileName: String, photo: ByteArray) {
        File(photoDirectory, fileName).outputStream().useInBackground { outputStream ->
            outputStream.write(photo)
        }
    }

    suspend fun getJpegImage(imageName: String): ByteArray? {
        return getImage("$imageName.jpeg")
    }

    suspend fun getImage(imageName: String): ByteArray? {
        return try {
            File(photoDirectory, imageName).inputStream().useInBackground { input ->
                input.readAllBytes()
            }
        } catch (e: FileNotFoundException) {
            println(e)
            null
        }
    }

    fun deleteImage(imageName: String): Boolean {
        return File(photoDirectory, imageName).delete()
    }

    fun existsImage(imageName: String): Boolean {
        return File(photoDirectory, imageName).isFile()
    }
}
