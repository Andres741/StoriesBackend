package com.conde.stories.service

import org.springframework.stereotype.Service
import java.io.File

@Service
class ImageDataService {

    private val photoDirectory = File("storiesPhotos")

    init {
        println("storiesPhotos directory is directory: ${photoDirectory.isDirectory}")
        if (!photoDirectory.isDirectory) {
            photoDirectory.delete().also {
                println("storiesPhotos directory deleted: $it")
            }
            photoDirectory.mkdir().also {
                println("storiesPhotos directory created: $it")
            }
        }
    }

    fun saveAsJpegImage(imageName: String, photo: ByteArray) {
        saveImage("$imageName.jpeg", photo)
    }

    fun saveImage(imageName: String, photo: ByteArray) {
        File(photoDirectory, imageName).outputStream().use { outputStream ->
            outputStream.write(photo)
        }
    }

    fun getJpegImage(imageName: String): ByteArray {
        return getImage("$imageName.jpeg")
    }

    fun getImage(imageName: String): ByteArray {
        return File(photoDirectory, imageName).inputStream().use { input ->
            input.readAllBytes()
        }
    }

    fun deleteImage(imageName: String): Boolean {
        return File(photoDirectory, imageName).delete()
    }

    fun existsImage(imageName: String): Boolean {
        return File(photoDirectory, imageName).isFile()
    }
}
