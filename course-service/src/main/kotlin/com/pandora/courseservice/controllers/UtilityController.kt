package com.pandora.courseservice.controllers

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationListener
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

@RestController
@RequestMapping("utility")
class UtilityController {

    private val log = LoggerFactory.getLogger(ApplicationListener::class.java)

    @Value("\${pandora.services.gateway.url}")
    private val gatewayUrl: String = ""

    @Value("\${server.port}")
    private val port: String = ""

    /*
    /   This forces service discovery for subject service
    */
    @GetMapping("/gateway")
    fun getGatewayConnectionStatus(): String {
        val result = RestTemplate().getForEntity("$gatewayUrl/service/subject?port=$port", String::class.java)
        return result.body.toString()
    }
}
