package com.pandora.userservice

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication
import java.net.InetAddress

@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
class UserServiceApplication

fun main(args: Array<String>) {
    val log: Logger = LoggerFactory.getLogger(UserServiceApplication::class.java)
    val context = runApplication<UserServiceApplication>(*args)

    log.info(
        "Local: http://127.0.0.1:{}",
        context.environment.getProperty("server.port", "8080")
    )

    log.info(
        "External: http://{}:{}",
        InetAddress.getLocalHost().hostAddress,
        context.environment.getProperty("server.port", "8080")
    )
}
