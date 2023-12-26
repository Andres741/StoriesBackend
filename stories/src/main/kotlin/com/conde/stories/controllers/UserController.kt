package com.conde.stories.controllers

import com.conde.stories.service.UserService
import com.conde.stories.service.model.UserDto
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("api/users/v1")
class UserController(private val service: UserService) {

    @PostMapping("user")
    fun createUser(
        @RequestParam userName: String,
        @RequestParam description: String,
        @RequestParam profileImage: String?
    ): UserDto = service.createUser(userName, description, profileImage)
        ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "User name cannot be blank")

    @GetMapping("user/{userId}")
    fun getUser(@PathVariable userId: String): UserDto = service.getUser(userId)
        ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    @DeleteMapping("user/{userId}")
    fun deleteUser(@PathVariable userId: String): UserDto = service.deleteUser(userId)
        ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    @GetMapping("all")
    fun getAllUser(): List<UserDto> = service.getAllUsers()
}
