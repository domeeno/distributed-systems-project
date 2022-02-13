package com.pandora.identityservice.repositories

interface SubscriptionRepository {
    fun createBaseSubscription(userId: String): String?
}