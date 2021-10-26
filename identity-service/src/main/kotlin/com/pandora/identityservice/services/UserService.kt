package com.pandora.identityservice.services

import com.pandora.identityservice.dto.RegisterDTO
import com.pandora.identityservice.models.User
import com.pandora.identityservice.repositories.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

    fun userCreate (dto: RegisterDTO) : User {
        val user = User();
        user.username = dto.username
        user.email = dto.email
        user.password = dto.password
        userRepository.save(user)
        return user;
    }
}