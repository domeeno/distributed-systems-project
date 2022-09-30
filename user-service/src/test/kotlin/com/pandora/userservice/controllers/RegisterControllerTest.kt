package com.pandora.userservice.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.ninjasquad.springmockk.MockkBean
import com.pandora.userservice.dto.UserDTO
import com.pandora.userservice.models.toUserEntity
import com.pandora.userservice.repository.UserRepository
import com.pandora.userservice.utils.readResourceIntoString
import io.mockk.every
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.io.ResourceLoader
import java.util.*


@MockkBean(UserRepository::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RegisterControllerTest(
    @Autowired private val restTemplate: TestRestTemplate,
    @Autowired private val resourceLoader: ResourceLoader,
    @Autowired private val repository: UserRepository,
    @Autowired private val registerController: RegisterController
) {

    val mockPath = "classpath:mocks/controllers"

    @Test
    @Throws(Exception::class)
    fun contextLoads() {
        assertThat(registerController).isNotNull
    }

    @Test
    fun createUser() {
        // data prepare
        val input = readResourceIntoString(
            resourceLoader,
            "$mockPath/UserDTO.json"
        )

        val readInput = jacksonObjectMapper().findAndRegisterModules().readValue<UserDTO>(input)
        val savedUser = readInput.toUserEntity()
        val uuid = UUID.randomUUID()

        savedUser.userId = uuid

        // mock repository response
        every { repository.save(any()) } returns savedUser

        // test
        val result = registerController.createUser(readInput)
        assert(result == uuid.toString())
    }
}