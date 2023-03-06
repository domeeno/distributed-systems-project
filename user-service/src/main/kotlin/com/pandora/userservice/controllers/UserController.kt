package com.pandora.userservice.controllers

import com.pandora.userservice.dto.EditUserDTO
import com.pandora.userservice.dto.UserInfoDTO
import com.pandora.userservice.dto.UserLoginDTO
import com.pandora.userservice.models.User
import com.pandora.userservice.models.toInfo
import com.pandora.userservice.repository.UserRepository
import com.pandora.userservice.service.UserService
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
import java.util.UUID

@RestController
@RequestMapping("/user")
class UserController(
    @Autowired private val userService: UserService,
    @Autowired private val userRepository: UserRepository,
) {

    @PostMapping("/login")
    fun loginUser(@RequestBody loginDTO: UserLoginDTO): String {
        return userService.loginUser(loginDTO)
    }

    @GetMapping("{userId}/info")
    fun getUserInfo(@PathVariable userId: String): UserInfoDTO {
        return userService.getUserInfo(userId)
    }

    @PutMapping("{userId}")
    fun updateUser(@PathVariable userId: String, @RequestBody userEdit: EditUserDTO): String {
        return userService.updateUser(userId, userEdit)
    }
}
