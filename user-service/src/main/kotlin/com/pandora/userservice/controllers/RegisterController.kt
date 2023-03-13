package com.pandora.userservice.controllers

import com.pandora.userservice.dto.UserDTO
import com.pandora.userservice.dto.UserLoginDTO
import com.pandora.userservice.exceptions.ApiException
import com.pandora.userservice.models.toUserEntity
import com.pandora.userservice.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class RegisterController(
    @Autowired private val userService: UserService,
) {

    @PostMapping("register")
    fun createUser(@RequestBody dto: UserDTO): String {
        throw ApiException("Deliberate Error", null, HttpStatus.I_AM_A_TEAPOT)
    }

    @PostMapping("login")
    fun loginUser(@RequestBody dto: UserLoginDTO): String {
        throw ApiException("Deliberate Error", null, HttpStatus.I_AM_A_TEAPOT)
    }
}
