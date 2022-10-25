package com.pandora.userservice.models

import com.pandora.userservice.dto.EditUserDTO
import com.pandora.userservice.dto.UserDTO
import com.pandora.userservice.dto.UserInfoDTO

fun UserDTO.toUserEntity(): User {
    val user = User()
    user.firstname = this.firstName
    user.lastname = this.lastName
    user.email = this.email
    user.dateOfBirth = this.dateOfBirth
    return user
}

fun User.toInfo(): UserInfoDTO {
    return UserInfoDTO(
        lastName = this.lastname,
        firstName = this.firstname,
        subjectsId = this.subjectsId,
        bio = this.bio
    )
}

fun User.toEditDTO(): EditUserDTO {
    return EditUserDTO(
        email = this.email,
        firstName = this.firstname,
        lastName = this.lastname,
        dateOfBirth = this.dateOfBirth,
        bio = this.bio,
        userId = this.userId.toString()
    )
}
