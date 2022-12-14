package com.pandora.userservice.dto

import java.sql.Date
import java.util.UUID

data class UserDTO(
    val userId: String?,
    val email: String,
    val firstName: String,
    val lastName: String,
    val password: String,
    val dateOfBirth: Date
)

data class EditUserDTO(
    val userId: String?,
    val email: String?,
    val firstName: String?,
    val lastName: String?,
    val dateOfBirth: Date?,
    val bio: String?
)

data class UserInfoDTO(
    val lastName: String,
    val firstName: String,
    val bio: String?,
    val subjectsId: UUID
)

data class UserLoginDTO(
    val email: String,
    val password: String
)

data class UserEntryDTO(
    val likedId: UUID,
    val savedId: UUID,
    val subjectsId: UUID
)
