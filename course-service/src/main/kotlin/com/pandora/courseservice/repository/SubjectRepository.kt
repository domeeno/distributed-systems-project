package com.pandora.courseservice.repository

import com.pandora.courseservice.models.Subject
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface SubjectRepository : MongoRepository<Subject, String>
