package com.pandora.identityservice.controllers

import com.pandora.identityservice.dto.RegisterDTO
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@Controller
class RegisterController {

    private val logger = LoggerFactory.getLogger(RegisterController::class.java)

    @PostMapping("/register")
    fun register(@RequestBody dto: RegisterDTO) {
        logger.info("new registration with email: " + dto.email)
    }
}