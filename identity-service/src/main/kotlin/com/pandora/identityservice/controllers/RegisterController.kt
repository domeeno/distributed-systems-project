package com.pandora.identityservice.controllers

import com.pandora.identityservice.dto.RegisterDTO
import com.pandora.identityservice.models.User
import com.pandora.identityservice.services.UserService
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@Controller
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
            ResponseEntity("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}