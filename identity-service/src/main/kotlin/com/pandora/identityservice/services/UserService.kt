package com.pandora.identityservice.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.pandora.identityservice.dto.LoginDTO
import com.pandora.identityservice.dto.RegisterDTO
import com.pandora.identityservice.models.User
import com.pandora.identityservice.repositories.UserRepository
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(private val userRepository: UserRepository) {

    @Value("\${spring.jwt.secret-key}")
    private val secretKey: String? = "secret"

    val passwordEncoder = BCryptPasswordEncoder()
    private val logger = LoggerFactory.getLogger(UserService::class.java)

    fun userCreate (dto: RegisterDTO) : User {
        val user = User();
        user.username = dto.username
        user.email = dto.email
        user.password = passwordEncoder.encode(dto.password)
        userRepository.save(user)
        return user;
    }

    fun authenticate(dto: LoginDTO) : String {
        val user = if (dto.email != null) {
            userRepository.findByEmail(dto.email)
        } else {
            userRepository.findByUsername(dto.username)
        }

        if (user == null) {
            logger.warn("User with ${if (dto.email != null) "email " else "username "}" + (dto.email ?: dto.username) + " not found")
            throw Exception("Wrong username or password")
        }

        val claims : HashMap<String, Any?> = HashMap<String, Any?>();
        claims["username"] = user.username;
        claims["scope"] = "default";

        val jwt = Jwts.builder()
            .setIssuer(user.userId.toString())
            .setExpiration(Date(System.currentTimeMillis() + 60 * 60 + 1000)) //hour for now
            .setClaims(claims)
            .signWith(SignatureAlgorithm.HS256, secretKey)

        return if (passwordEncoder.matches(dto.password, user.password)) jwt.compact() else throw Exception("Wrong username or password")
    }
}