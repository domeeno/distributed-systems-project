package com.pandora.identityservice.dto

import java.sql.Date

data class RegisterDTO (
    val email: String,
    val username: String,
    val password: String,
    val firstname: String,
    val dateOfBirth: Date,
    val lastname: String,
)