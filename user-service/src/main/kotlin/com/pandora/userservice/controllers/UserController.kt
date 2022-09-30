package com.pandora.userservice.controllers

import com.pandora.userservice.dto.UserDTO
import com.pandora.userservice.dto.UserLoginDTO
import com.pandora.userservice.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/user")
class UserController(
    @Autowired private val userRepository: UserRepository
) {

    @PostMapping("/login")
    fun loginUser(@RequestBody loginDTO: UserLoginDTO): ResponseEntity<String> {
        return ResponseEntity.ok("TODO")
    }

    @PutMapping("/edit")
    fun editUser(@RequestBody userDTO: UserDTO): ResponseEntity<String> {
        return ResponseEntity.ok("TODO")
    }
}
