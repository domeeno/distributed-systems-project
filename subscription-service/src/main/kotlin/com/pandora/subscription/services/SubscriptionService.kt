package com.pandora.subscription.services

import com.pandora.subscription.models.SubscriptionModel
import com.pandora.subscription.repositories.PackRepository
import com.pandora.subscription.repositories.SubscriptionsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class SubscriptionService(
    @Autowired private val subscriptionsRepository: SubscriptionsRepository,
    @Autowired private val packRepository: PackRepository
    ) {

    fun createBasePackForUser(userId: String): String {
        val subscriptions = SubscriptionModel()
        val pack = packRepository.findByName("Base");

        if (pack != null) {
            subscriptions.packId = pack.id
        }

        subscriptions.userId = UUID.fromString(userId)
        subscriptions.members.add(UUID.fromString(userId))
        subscriptions.months = 0
        subscriptionsRepository.save(subscriptions)
        return subscriptions.id.toString()
    }

}