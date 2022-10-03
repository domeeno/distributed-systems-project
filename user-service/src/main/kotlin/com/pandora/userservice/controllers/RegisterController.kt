package com.pandora.userservice.controllers

import com.pandora.userservice.dto.UserDTO
import com.pandora.userservice.models.toUserEntity
import com.pandora.userservice.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("register")
class RegisterController(
    @Autowired private val userRepository: UserRepository
) {

    val passwordEncoder = BCryptPasswordEncoder()
    private val log = LoggerFactory.getLogger(RegisterController::class.java)

    @PostMapping
    fun createUser(@RequestBody userDto: UserDTO): ResponseEntity<String> {
        val user = userDto.toUserEntity()
        user.password = passwordEncoder.encode(userDto.password)

        val savedUser = userRepository.save(user)

        return ResponseEntity.ok(savedUser.userId.toString())
    }
}
