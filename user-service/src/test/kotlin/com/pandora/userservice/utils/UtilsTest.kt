package com.pandora.userservice.utils

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.pandora.userservice.models.User
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ResourceLoader

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UtilsTest(
    @Autowired private val resourceLoader: ResourceLoader
) {

    private val mockPath = "classpath:mocks/controllers"

    @Test
    fun `jwt generate`() {
        val input = readResourceIntoString(
            resourceLoader,
            "$mockPath/User.json"
        )

        val readInput = jacksonObjectMapper().findAndRegisterModules().readValue<User>(input)

        val result = generateJwt(readInput)

        // TODO assertions
    }
}
