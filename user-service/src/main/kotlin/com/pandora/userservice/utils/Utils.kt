package com.pandora.userservice.utils

import com.pandora.userservice.models.User
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.apache.commons.io.IOUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component
import java.io.IOException
import java.util.Date

@Component
class Utils {
    @Value("\${security.jwt.secret}")
    private val secret: String = ""

    @Value("\${server.port}")
    private val port: String = ""

    fun generateJwt(user: User): String {
        val claims: HashMap<String, Any?> = HashMap()
        claims["email"] = user.email
        claims["userId"] = user.userId
        claims["likes"] = user.likedId
        claims["saves"] = user.savedId
        claims["subjects"] = user.subjectsId

        return Jwts.builder()
            .setIssuer("identity:$port")
            .setExpiration(Date(System.currentTimeMillis() + 60 * 60 + 1000)) // hour from now (for now)
            .setClaims(claims)
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact()
    }
}

@Throws(IOException::class)
fun readResourceIntoString(resourceLoader: ResourceLoader, file: String): String {
    val resource = resourceLoader.getResource(file)
    return IOUtils.toString(resource.inputStream, "UTF-8")
}
