package com.pandora.userservice.service

import com.pandora.userservice.models.User

interface UserService {

    fun registerUser(user: User): String
}
