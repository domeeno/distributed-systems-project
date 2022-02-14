package com.pandora.identityservice.repositories

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.stereotype.Repository
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder


@Repository
class SubscriptionRepositoryImpl: SubscriptionRepository {
    @Value("\${pandora.services.subscription.url}")
    private val subscriptionUrl: String = ""

    override fun createBaseSubscription(userId: String): String? {
        val headers = HttpHeaders()
        headers[HttpHeaders.ACCEPT] = MediaType.APPLICATION_JSON_VALUE
        val entity: HttpEntity<*> = HttpEntity<Any>(headers)

        val urlTemplate = UriComponentsBuilder.fromHttpUrl("$subscriptionUrl/subscription")
            .queryParam("userId", userId)
            .encode()
            .toUriString()

        val params: MutableMap<String, String> = HashMap<String, String>()
        params["userId"] = userId

        val result: ResponseEntity<String> = RestTemplate().exchange(
            urlTemplate,
            HttpMethod.POST,
            entity,
            String::class.java,
            params
        )

        return result.body
    }

}