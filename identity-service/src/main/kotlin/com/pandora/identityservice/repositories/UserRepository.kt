package com.pandora.identityservice.repositories

import com.pandora.identityservice.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, String> {

}