package com.pandora.userservice.repository

import com.pandora.userservice.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface UserRepository : JpaRepository<User, String> {
    fun findByEmail(email: String): Optional<User>
    fun findByUserId(userId: UUID): Optional<User>
}
