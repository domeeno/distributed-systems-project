package com.pandora.subscription.repositories

import com.pandora.subscription.models.SubscriptionModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SubscriptionsRepository: JpaRepository<SubscriptionModel, String> {

}