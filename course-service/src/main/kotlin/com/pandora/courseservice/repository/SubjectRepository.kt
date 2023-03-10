package com.pandora.courseservice.repository

import com.pandora.courseservice.models.Subject
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface SubjectRepository : MongoRepository<Subject, String> {
    fun findBySubjectNameContainingIgnoreCase(name: String, pageable: Pageable): List<Subject>
}
