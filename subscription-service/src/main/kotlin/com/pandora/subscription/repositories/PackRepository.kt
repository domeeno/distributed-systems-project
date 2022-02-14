package com.pandora.subscription.repositories

import com.pandora.subscription.models.PackModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PackRepository: JpaRepository<PackModel, String> {
    fun findByName(name: String): PackModel?
}