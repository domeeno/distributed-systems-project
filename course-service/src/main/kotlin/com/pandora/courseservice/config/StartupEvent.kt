package com.pandora.userservice.config

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.net.InetAddress

@Component
class StartupEvent : ApplicationListener<ContextRefreshedEvent> {
    @Value("\${server.port}")
    private val port: String = ""

    @Value("\${pandora.services.gateway.url")
    private val gatewayUrl: String = ""

    private val log = LoggerFactory.getLogger(ApplicationListener::class.java)

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        log.info("This is working: ${InetAddress.getLoopbackAddress().hostName}")
        try {
            val params: MutableMap<String, String> = HashMap()
            params["port"] = port

            val headers = HttpHeaders()
            headers.set("Accept", "application/json")

            val entity: HttpEntity<*> = HttpEntity<Any>(headers)

            val url = UriComponentsBuilder.fromHttpUrl("$gatewayUrl/service/user")
                .encode()
                .toUriString()

            val result: ResponseEntity<String> = RestTemplate().postForEntity(url, entity, String::class.java, params)
            log.info(result.body)
            log.info(result.statusCode.toString())
        } catch (e: java.lang.Exception) {
            log.error("error", e)
        }
    }
}
