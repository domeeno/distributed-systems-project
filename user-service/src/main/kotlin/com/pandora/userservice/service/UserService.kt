package com.pandora.userservice.service

import com.pandora.userservice.dto.UserInfoDTO
import com.pandora.userservice.dto.UserLoginDTO
import com.pandora.userservice.models.User

interface UserService {

    fun registerUser(user: User): String

    fun loginUser(loginDTO: UserLoginDTO): String

    fun getUserInfo(userId: String): UserInfoDTO

    fun updateUser(userId: String): String
}
