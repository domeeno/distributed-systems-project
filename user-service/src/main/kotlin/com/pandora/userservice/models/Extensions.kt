package com.pandora.userservice.models

import com.pandora.userservice.dto.UserDTO

fun UserDTO.toUserEntity(): User {
    val user = User()
    user.firstName = this.firstName
    user.lastName = this.lastName
    user.email = this.email
    user.dateOfBirth = this.dateOfBirth
    return user
}