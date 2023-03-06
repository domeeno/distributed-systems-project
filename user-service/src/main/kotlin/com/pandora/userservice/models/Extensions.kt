package com.pandora.userservice.models

import com.pandora.userservice.dto.EditUserDTO
import com.pandora.userservice.dto.UserDTO
import com.pandora.userservice.dto.UserInfoDTO

fun UserDTO.toUserEntity(): User {
    return User(
        email = email,
        firstname = firstName,
        lastname = lastName,
        password = password,
        dateOfBirth = dateOfBirth
    )
}

fun User.toInfo(): UserInfoDTO {
    return UserInfoDTO(
        lastName = lastname,
        firstName = firstname,
        subjectsId = subjectsId,
        bio = bio
    )
}

fun User.toEditDTO(): EditUserDTO {
    return EditUserDTO(
        email = email,
        firstName = firstname,
        lastName = lastname,
        dateOfBirth = dateOfBirth,
        bio = bio,
        userId = userId.toString()
    )
}
