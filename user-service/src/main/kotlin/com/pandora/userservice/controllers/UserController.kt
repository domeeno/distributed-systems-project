package com.pandora.userservice.controllers

import com.pandora.userservice.dto.UserDTO
import com.pandora.userservice.dto.UserLoginDTO
import com.pandora.userservice.models.User
import com.pandora.userservice.repository.UserRepository
import com.pandora.userservice.utils.generateJwt
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/user")
class UserController(
    @Autowired private val userRepository: UserRepository
) {

    private val passwordEncoder = BCryptPasswordEncoder()
    private val log = LoggerFactory.getLogger(UserController::class.java)

    @PostMapping("/login")
    fun loginUser(@RequestBody loginDTO: UserLoginDTO): ResponseEntity<String> {
        val oUser = userRepository.findByEmail(loginDTO.email)
        val user: User
        if (oUser.isEmpty) {
            log.error("User with email not found")
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Wrong username or password")
        } else {
            user = oUser.get()
        }

        return if (!passwordEncoder.matches(
                loginDTO.password,
                user.password
            )
        ) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Wrong username or password")
        } else {
            ResponseEntity.ok(generateJwt(user))
        }
    }

    // TESTING REASONS WILL BE MODIFIED
    @GetMapping("/getUsers")
    fun getUsers(): ResponseEntity<MutableList<User>> {
        return ResponseEntity.ok().body(userRepository.findAll())
    }

    @PutMapping("/edit")
    fun editUser(@RequestBody userDTO: UserDTO): ResponseEntity<String> {
        return ResponseEntity.ok("TODO")
    }
}
