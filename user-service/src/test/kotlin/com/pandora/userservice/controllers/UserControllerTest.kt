package com.pandora.userservice.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.ninjasquad.springmockk.MockkBean
import com.pandora.userservice.dto.UserLoginDTO
import com.pandora.userservice.models.User
import com.pandora.userservice.repository.UserRepository
import com.pandora.userservice.utils.readResourceIntoString
import io.mockk.every
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ResourceLoader
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.context.WebApplicationContext
import java.util.Optional

@MockkBean(UserRepository::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerTest(
    @Autowired private val userController: UserController,
    @Autowired private val resourceLoader: ResourceLoader,
    @Autowired private val repository: UserRepository,
    @Autowired private val applicationContext: WebApplicationContext
) {

    val mockPath = "classpath:mocks/controllers"
    val passwordEncoder = BCryptPasswordEncoder()

    @Test
    @Throws(Exception::class)
    fun contextLoads() {
        Assertions.assertThat(userController).isNotNull
    }

    @Test
    fun editUser() {
    }
}
