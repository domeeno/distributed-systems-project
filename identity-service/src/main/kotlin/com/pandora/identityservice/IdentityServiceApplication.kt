package com.pandora.identityservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
class IdentityServiceApplication

fun main(args: Array<String>) {
	runApplication<IdentityServiceApplication>(*args)
}
