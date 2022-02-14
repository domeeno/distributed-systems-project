package com.pandora.subscription.controllers

import com.pandora.subscription.services.SubscriptionService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*


@Controller
@RequestMapping("subscription")
class SubscriptionController(private val subscriptionService: SubscriptionService) {
    private val logger = LoggerFactory.getLogger(SubscriptionController::class.java)


    @PostMapping("")
    fun createBasePackForUser(@RequestParam userId: String) : ResponseEntity<*> {
        return try {
            logger.info("new pack for: $userId")
            val packId = subscriptionService.createBasePackForUser(userId);
            logger.info("pack registration for $userId succeeded")
            ResponseEntity(packId, HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity("Something went wrong: " + e.message, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}