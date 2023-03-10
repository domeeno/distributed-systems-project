package com.pandora.userservice.models

import com.pandora.userservice.dto.UserDTO
import com.pandora.userservice.dto.UserInfoDTO
import com.pandora.userservice.dto.UserLoginDTO

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

fun UserDTO.toPrivate(): UserDTO {
    return UserDTO(
        userId = "no id yet",
        email = email,
        firstName = firstName,
        lastName = lastName,
        password = "*********",
        dateOfBirth = dateOfBirth
    )
}

fun UserLoginDTO.toPrivate(): UserLoginDTO {
    return UserLoginDTO(
        email = email,
        password = "*********"
    )
}
