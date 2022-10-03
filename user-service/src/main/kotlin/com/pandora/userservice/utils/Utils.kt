package com.pandora.userservice.utils

import com.pandora.userservice.models.User
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.apache.commons.io.IOUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ResourceLoader
import java.io.IOException
import java.util.Date
import kotlin.collections.HashMap

@Value("\${spring.jwt.secret-key}")
private val secretKey: String = "secret"

@Throws(IOException::class)
fun readResourceIntoString(resourceLoader: ResourceLoader, file: String): String {
    val resource = resourceLoader.getResource(file)
    return IOUtils.toString(resource.inputStream, "UTF-8")
}

fun generateJwt(user: User): String {
    val claims: HashMap<String, Any?> = HashMap<String, Any?>()
    claims["email"] = user.email
    claims["roles"] = "user"

    return Jwts.builder()
        .setIssuer("identity")
        .setExpiration(Date(System.currentTimeMillis() + 60 * 60 + 1000)) // hour for now
        .setClaims(claims)
        .signWith(SignatureAlgorithm.HS256, secretKey)
        .compact()
}
