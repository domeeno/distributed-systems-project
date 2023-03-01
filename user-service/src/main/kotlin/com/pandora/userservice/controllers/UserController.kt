package com.pandora.userservice.controllers

import com.pandora.userservice.dto.EditUserDTO
import com.pandora.userservice.dto.UserInfoDTO
import com.pandora.userservice.dto.UserLoginDTO
import com.pandora.userservice.models.User
import com.pandora.userservice.models.toInfo
import com.pandora.userservice.repository.UserRepository
import com.pandora.userservice.utils.Utils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.sql.Timestamp
import java.util.UUID

@RestController
@RequestMapping("/user")
class UserController(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val utils: Utils
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
            ResponseEntity.ok(utils.generateJwt(user))
        }
    }

    @GetMapping("{userId}/info")
    fun getUserInfo(@PathVariable userId: String): ResponseEntity<UserInfoDTO> {
        return ResponseEntity.ok(userRepository.findByUserId(UUID.fromString(userId)).get().toInfo())
    }

    @PutMapping("{userId}")
    fun updateUser(@PathVariable userId: String, @RequestBody userEdit: EditUserDTO): ResponseEntity<String> {
        val dbUser = userRepository.findByUserId(UUID.fromString(userId)).get()

        dbUser.email = userEdit.email ?: dbUser.email
        dbUser.firstname = userEdit.firstName ?: dbUser.firstname
        dbUser.lastname = userEdit.lastName ?: dbUser.lastname
        dbUser.dateOfBirth = userEdit.dateOfBirth ?: dbUser.dateOfBirth
        dbUser.bio = userEdit.bio ?: dbUser.bio
        dbUser.updateTimestamp = Timestamp(System.currentTimeMillis())

        userRepository.save(dbUser)

        return ResponseEntity.ok(dbUser.userId.toString())
    }
}
