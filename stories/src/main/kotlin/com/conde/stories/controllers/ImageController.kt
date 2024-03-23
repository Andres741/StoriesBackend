package com.conde.stories.controllers

import com.conde.stories.service.ImageDataService
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("api/images")
class ImageController(private val imageDataService: ImageDataService) {

    @GetMapping(value = ["/{imageName}"], produces = [MediaType.IMAGE_JPEG_VALUE])
    fun getImage(@PathVariable imageName: String): ResponseEntity<ByteArrayResource>? {
        val image = imageDataService.getImage(imageName)
        val resource = ByteArrayResource(image, "$imageName.jpeg")
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentLength(resource.contentLength())
            .body(resource)
    }

    @PostMapping
    fun uploadImage(@RequestParam("image") file: MultipartFile) {
        val imageName = file.originalFilename ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Must have name")
        imageDataService.saveImage(imageName, file.bytes)
    }

    @DeleteMapping("/{imageName}")
    fun deleteImage(@PathVariable imageName: String) {
        if (!imageDataService.deleteImage(imageName)) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }
    }
}
