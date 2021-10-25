package com.pandora.identityservice.dto

data class RegisterDTO (
    val email: String,
    val username: String,
    val password: String,
)