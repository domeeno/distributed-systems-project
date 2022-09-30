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
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ResourceLoader
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import java.util.*


@MockkBean(UserRepository::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RegisterControllerTest(
    @Autowired private val resourceLoader: ResourceLoader,
    @Autowired private val repository: UserRepository,
    @Autowired private val registerController: RegisterController,
    @Autowired private val applicationContext: WebApplicationContext
) {

    private lateinit var mockMvc: MockMvc

    @BeforeAll
    fun setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build()
    }

    val mockPath = "classpath:mocks/controllers"

    @Test
    @Throws(Exception::class)
    fun contextLoads() {
        assertThat(registerController).isNotNull
    }

    @Test
    fun `test createUser logic`() {
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
        assert(result == ResponseEntity.ok(uuid.toString()))
    }

    @Test
    fun `test post register new user`() {
        // data prepare
        val input = readResourceIntoString(
            resourceLoader,
            "$mockPath/UserDTO.json"
        )

        val readInput = jacksonObjectMapper().findAndRegisterModules().readValue<UserDTO>(input)
        val savedUser = readInput.toUserEntity()
        val uuid = UUID.randomUUID()

        savedUser.userId = uuid
        every { repository.save(any()) } returns savedUser

        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .post("/register")
                    .content(input)
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk)
    }
}