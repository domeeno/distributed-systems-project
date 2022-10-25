package com.pandora.courseservice.repository

import com.pandora.courseservice.models.UserSubjects
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserSubjectsRepository: MongoRepository<UserSubjects, String> {
}