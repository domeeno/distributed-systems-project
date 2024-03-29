package com.pandora.userservice.controllers

import com.pandora.userservice.dto.EditUserDTO
import com.pandora.userservice.dto.UserInfoDTO
import com.pandora.userservice.repository.UserRepository
import com.pandora.userservice.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(
    @Autowired private val userService: UserService,
    @Autowired private val userRepository: UserRepository,
) {

    @GetMapping("{userId}/info")
    fun getUserInfo(@PathVariable userId: String): UserInfoDTO {
        return userService.getUserInfo(userId)
    }

    @PutMapping("{userId}")
    fun updateUser(@PathVariable userId: String, @RequestBody dto: EditUserDTO): String {
        return userService.updateUser(userId, dto)
    }
}
