package com.pandora.userservice.controllers

import com.pandora.userservice.dto.UserDTO
import com.pandora.userservice.models.toUserEntity
import com.pandora.userservice.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("register")
class RegisterController(
    @Autowired private val userService: UserService,
) {

    @PostMapping
    fun createUser(@RequestBody userDto: UserDTO): String {
        return userService.registerUser(userDto.toUserEntity())
    }
}
