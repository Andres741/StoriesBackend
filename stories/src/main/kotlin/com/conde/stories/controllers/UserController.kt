package com.conde.stories.controllers

import com.conde.stories.service.UserService
import com.conde.stories.service.model.UserDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("api/users/v1")
class UserController(
    private val service: UserService,
    private val objectMapper: ObjectMapper,
) {

    @PostMapping("user")
    suspend fun createUser(
        @RequestParam userName: String,
        @RequestParam description: String,
        @RequestParam("profileImage") file: MultipartFile?,
    ): UserDto = service.createUser(userName = userName, description = description, profileImageData = file?.bytes)
        ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "User name cannot be blank")

    @PostMapping("edit")
    suspend fun editUser(
        @RequestParam("user") userJson: String,
        @RequestParam("profileImage") file: MultipartFile?,
    ): UserDto {
        val user = objectMapper.readValue(userJson, UserDto::class.java)
        return service.editUser(user, file?.bytes)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Requested user does not exists")
    }

    @GetMapping("user/{userId}")
    fun getUser(@PathVariable userId: String): UserDto = service.getUser(userId)
        ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    @DeleteMapping("user/{userId}")
    fun deleteUser(@PathVariable userId: String): UserDto = service.deleteUser(userId)
        ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    @GetMapping("all")
    fun getAllUser(): List<UserDto> = service.getAllUsers()
}
