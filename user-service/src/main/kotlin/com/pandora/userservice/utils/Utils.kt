package com.pandora.userservice.utils

import com.pandora.userservice.models.User
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.apache.commons.io.IOUtils
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ResourceLoader
import org.springframework.data.crossstore.ChangeSetPersister
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException.Unauthorized
import org.springframework.web.server.ResponseStatusException
import java.io.IOException
import java.util.Date
import javax.servlet.http.HttpServletRequest


@Value("\${spring.jwt.secret-key}")
private val secretKey: String = "secret"

@Throws(IOException::class)
fun readResourceIntoString(resourceLoader: ResourceLoader, file: String): String {
    val resource = resourceLoader.getResource(file)
    return IOUtils.toString(resource.inputStream, "UTF-8")
}

fun generateJwt(user: User): String {
    val claims: HashMap<String, Any?> = HashMap()
    claims["email"] = user.email
    claims["userId"] = user.userId
    claims["likes"] = user.likedId
    claims["saves"] = user.savedId
    claims["subjects"] = user.subjectsId

    return Jwts.builder()
        .setIssuer("identity")
        .setExpiration(Date(System.currentTimeMillis() + 60 * 60 + 1000)) // hour from now (for now)
        .setClaims(claims)
        .signWith(SignatureAlgorithm.HS256, secretKey)
        .compact()
}
