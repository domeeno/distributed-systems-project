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

data class UserLoginDTO(
    val email: String,
    val password: String
)

data class UserEntryDTO(
    val likedId: UUID,
    val savedId: UUID
)
