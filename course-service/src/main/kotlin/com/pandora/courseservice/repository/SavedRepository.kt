package com.pandora.courseservice.repository

import com.pandora.courseservice.models.Saved
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface SavedRepository : MongoRepository<Saved, String>
