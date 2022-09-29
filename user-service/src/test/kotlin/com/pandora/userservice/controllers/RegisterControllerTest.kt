package com.pandora.userservice.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.pandora.userservice.dto.UserDTO
import com.pandora.userservice.utils.readResourceIntoString
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.core.io.ResourceLoader
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RegisterControllerTest(
    @Autowired val restTemplate: TestRestTemplate,
    @Autowired val resourceLoader: ResourceLoader
) {

    val mockPath = "classpath:mocks/controllers"

    @Test
    fun createUser() {
        val input = readResourceIntoString(
            resourceLoader,
            "$mockPath/UserDTO.json"
        )

        val readInput = jacksonObjectMapper().findAndRegisterModules().readValue<UserDTO>(input)

        val entity = restTemplate.postForEntity<UserDTO>("/register", readInput)
        assert(entity.statusCode == HttpStatus.OK)
        assert(entity.body!!.email == "dominicflocea.test@gmail.com")
    }
}