package com.pandora.identityservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class IdentityServiceApplication

fun main(args: Array<String>) {
	runApplication<IdentityServiceApplication>(*args)
}
