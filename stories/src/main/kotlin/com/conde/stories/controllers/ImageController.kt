package com.conde.stories.controllers

import com.conde.stories.controllers.ImageController.Companion.IMAGES_API_PATH
import com.conde.stories.service.ImageDataService
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping(IMAGES_API_PATH)
class ImageController(private val imageDataService: ImageDataService) {

    companion object {
        const val IMAGES_API_PATH = "api/images"
    }

    @GetMapping(value = ["/{imageName}"], produces = [MediaType.IMAGE_JPEG_VALUE])
    suspend fun getImage(@PathVariable imageName: String): ResponseEntity<ByteArrayResource>? {
        val image = imageDataService.getImage(imageName) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        val resource = ByteArrayResource(image, imageName)
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentLength(resource.contentLength())
            .body(resource)
    }

    @PostMapping
    fun uploadNewImage(@RequestParam("newImage") file: MultipartFile): Map<String, String> {
        val imageName = imageDataService.saveAsNewJpegImage(file.name, file.bytes)
        return mapOf("imageName" to imageName)
    }

    @PostMapping("test")
    suspend fun uploadTestingImage(@RequestParam("testImage") file: MultipartFile): Map<String, String> {
        val imageName = "testImage.jpeg"
        imageDataService.saveFile(imageName, file.bytes)
        return mapOf("imageName" to imageName)
    }

    @DeleteMapping("/{imageName}")
    fun deleteImage(@PathVariable imageName: String) {
        if (!imageDataService.deleteImage(imageName)) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }
    }
}
