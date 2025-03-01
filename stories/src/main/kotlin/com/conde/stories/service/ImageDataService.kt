package com.conde.stories.service

import com.conde.stories.infrastructure.util.createUUID
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileNotFoundException

@Service
class ImageDataService {

    private val photoDirectory = File("storiesPhotos")

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
        saveFile(jpegName, photo)
        return jpegName
    }

    fun saveFile(fileName: String, photo: ByteArray) {
        File(photoDirectory, fileName).outputStream().use { outputStream ->
            outputStream.write(photo)
        }
    }

    fun getJpegImage(imageName: String): ByteArray? {
        return getImage("$imageName.jpeg")
    }

    fun getImage(imageName: String): ByteArray? {
        return try {
            File(photoDirectory, imageName).inputStream().use { input ->
                input.readAllBytes()
            }
        } catch (e: FileNotFoundException) {
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
