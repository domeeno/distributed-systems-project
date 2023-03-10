package com.pandora.userservice.startup

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.web.client.RestTemplate

// @Component
class StartupEvent : ApplicationListener<ContextRefreshedEvent> {
    @Value("\${server.port}")
    private val port: String = ""

    @Value("\${pandora.services.gateway.url}")
    private val gatewayUrl: String = ""

    private val log = LoggerFactory.getLogger(ApplicationListener::class.java)

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        var registered = false
        log.info("[DISCOVERY]: Registering to $gatewayUrl")
        while (!registered) {
            try {
                val params: MutableMap<String, String> = HashMap()
                params["port"] = port

                // TODO replace with post
                val result = RestTemplate().getForEntity("$gatewayUrl/service/user?port=$port", String::class.java)

                // TODO replace hardcoded good string with enum
                registered = result.body.toString() == "Good"
            } catch (e: java.lang.Exception) {
                log.error("[DISCOVERY]: error registering service", e.message)
            }
            Thread.sleep(5000)
        }

        log.info("[DISCOVERY]: Service registered")
    }
}
