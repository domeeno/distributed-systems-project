package com.pandora.identityservice.repositories

import org.springframework.web.client.RestTemplate

class SubscriptionRepositoryImpl: SubscriptionRepository {
    override fun createBaseSubscription(userId: String): String? {
        val restTemplate = RestTemplate()

        return restTemplate.getForObject("http://localhost:8082/subscription", String::class.java)
    }

}