package com.pandora.identityservice.controllers

import com.pandora.identityservice.dto.LoginDTO
import com.pandora.identityservice.dto.RecoveryDTO
import com.pandora.identityservice.dto.RegisterDTO
import com.pandora.identityservice.dto.ResetDTO
import com.pandora.identityservice.models.User
import com.pandora.identityservice.services.UserService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("auth")
@CrossOrigin("http://localhost:3000")
class RegisterController(private val userService: UserService){

    private val logger = LoggerFactory.getLogger(RegisterController::class.java)

    @PostMapping("/register")
    fun register(@RequestBody dto: RegisterDTO) : ResponseEntity<*> {
        return try {
            logger.info("new registration with email: " + dto.email)
            val jwt = userService.userCreate(dto);
            logger.info("registration for ${dto.email} succeeded")
            ResponseEntity(jwt, HttpStatus.OK)
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

    @PostMapping("/recovery")
    fun recovery(@RequestBody dto: RecoveryDTO): ResponseEntity<*> {
        return try {
            logger.info("recovery for " + (dto.email ?: dto.username))
            val resetLink = userService.recovery(dto);
            logger.info("recovery for " + (dto.email ?: dto.username) + " successful")
            ResponseEntity(resetLink, HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity(
                "recovery with " + (dto.email ?: dto.username) + " unsuccessful: " + e.message,
                HttpStatus.INTERNAL_SERVER_ERROR
            )
        }
    }

    @PutMapping("/reset")
    fun update(@RequestBody dto: ResetDTO): ResponseEntity<*> {
        return try {
            logger.info("reset with " + (dto.email))
            val resetLink = userService.reset(dto);
            logger.info("reset with " + (dto.email) + " successful")
            ResponseEntity("", HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity(
                "reset with " + (dto.email) + " unsuccessful: " + e.message,
                HttpStatus.INTERNAL_SERVER_ERROR
            )
        }
    }
}