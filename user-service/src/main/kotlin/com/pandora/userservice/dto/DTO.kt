package com.pandora.userservice.dto

import java.sql.Date

data class UserDTO (
    val email: String,
    val firstName: String,
    val lastName: String,
    val password: String,
    val dateOfBirth: Date
)