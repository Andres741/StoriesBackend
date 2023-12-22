package com.conde.stories.controllers

import com.conde.stories.service.UserService
import com.conde.stories.service.model.UserDto
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/users/v1")
class UserController(private val service: UserService) {

    @PostMapping("user/{userName}")
    fun createUser(@PathVariable userName: String) = service.createUser(userName)

    @GetMapping("user/{userId}")
    fun getUser(@PathVariable userId: String) = service.getUser(userId)

    @DeleteMapping("user/{userId}")
    fun deleteUser(@PathVariable userId: String) = service.deleteUser(userId)

    @GetMapping("all")
    fun getAllUser(): List<UserDto> = service.getAllUsers()
}
