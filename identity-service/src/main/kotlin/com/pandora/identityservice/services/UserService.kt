package com.pandora.identityservice.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.pandora.identityservice.dto.LoginDTO
import com.pandora.identityservice.dto.RecoveryDTO
import com.pandora.identityservice.dto.RegisterDTO
import com.pandora.identityservice.dto.ResetDTO
import com.pandora.identityservice.models.User
import com.pandora.identityservice.repositories.SubscriptionRepository
import com.pandora.identityservice.repositories.UserRepository
import io.jsonwebtoken.JwtBuilder
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository,
    private val subscriptionRepository: SubscriptionRepository
    ) {

    @Value("\${spring.jwt.secret-key}")
    private val secretKey: String? = "secret"

    @Value("\${spring.services.url.front-end")
    private val url: String? = "http://localhost:3000"

    val passwordEncoder = BCryptPasswordEncoder()
    private val logger = LoggerFactory.getLogger(UserService::class.java)

    fun userCreate (dto: RegisterDTO) : String {
        val user = User();
        user.username = dto.username
        user.email = dto.email
        user.password = passwordEncoder.encode(dto.password)
        user.firstname = dto.firstname
        user.lastname = dto.lastname
        user.dateOfBirth = dto.dateOfBirth
        val savedUser = userRepository.save(user)
        subscriptionRepository.createBaseSubscription(savedUser.userId.toString())

        val jwt = generateJwt(user)

        return jwt.compact();
    }

    fun authenticate(dto: LoginDTO) : String {
        val user = if (dto.email != null) {
            userRepository.findByEmail(dto.email)
        } else {
            userRepository.findByUsername(dto.username)
        }

        if (user == null) {
            logger.warn(
                "User with ${if (dto.email != null) "email " else "username "}" + (dto.email
                    ?: dto.username) + " not found"
            )
            throw Exception("Wrong username or password")
        }

        val jwt = generateJwt(user)

        return if (passwordEncoder.matches(
                dto.password,
                user.password
            )
        ) jwt.compact() else throw Exception("Wrong username or password")
    }

    fun recovery(dto: RecoveryDTO): String {
        val user = if (dto.email != null) {
            userRepository.findByEmail(dto.email)
        } else {
            userRepository.findByUsername(dto.username)
        }

        if (user == null) {
            logger.warn(
                "User with ${if (dto.email != null) "email " else "username "}" + (dto.email
                    ?: dto.username) + " not found"
            )
            throw Exception("Wrong username or password")
        }

        val claims: HashMap<String, Any?> = HashMap<String, Any?>();
        claims["email"] = user.email;
        claims["reset"] = true;

        val jwt = Jwts.builder()
            .setIssuer("identity")
            .setExpiration(Date(System.currentTimeMillis() + 60 * 60 + 1000)) //hour for now
            .setClaims(claims)
            .signWith(SignatureAlgorithm.HS256, secretKey)

        return "${url}/confirm/$jwt"
    }

    fun reset(dto: ResetDTO) {

    }

    private fun generateJwt(user: User): JwtBuilder {
        val claims: HashMap<String, Any?> = HashMap<String, Any?>();
        claims["username"] = user.username;
        claims["email"] = user.email;
        claims["scope"] = "default";

        return Jwts.builder()
            .setIssuer("identity")
            .setExpiration(Date(System.currentTimeMillis() + 60 * 60 + 1000)) //hour for now
            .setClaims(claims)
            .signWith(SignatureAlgorithm.HS256, secretKey)
    }
}