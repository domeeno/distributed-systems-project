package com.pandora.userservice.controllers

import com.pandora.userservice.dto.UserDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping


@Controller
@RequestMapping("register")
class RegisterController {

    @PostMapping
    fun createUser(@RequestBody userDto: UserDTO): UserDTO {
        return userDto
    }
}