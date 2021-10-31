package com.pandora.identityservice.controllers

import com.pandora.identityservice.dto.LoginDTO
import com.pandora.identityservice.dto.RegisterDTO
import com.pandora.identityservice.models.User
import com.pandora.identityservice.services.UserService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("auth")
class RegisterController(private val userService: UserService){

    private val logger = LoggerFactory.getLogger(RegisterController::class.java)

    @PostMapping("/register")
    fun register(@RequestBody dto: RegisterDTO) : ResponseEntity<*> {
        return try {
            logger.info("new registration with email: " + dto.email)
            val user: User = userService.userCreate(dto);
            logger.info("registration for ${user.email} succeeded")
            ResponseEntity(dto, HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity("Something went wrong: " + e.message, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PostMapping("/login")
    fun login(@RequestBody dto: LoginDTO) : ResponseEntity<*> {
        return try {
            logger.info("login with " + (dto.email ?: dto.username))
            val jwt = userService.authenticate(dto);
            logger.info("login with " + (dto.email ?: dto.username) + " successful")
            ResponseEntity(jwt, HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity("login with " + (dto.email ?: dto.username) + " unsuccessful: " + e.message, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}