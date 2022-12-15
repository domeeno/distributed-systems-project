package com.pandora.courseservice.repository

import com.pandora.courseservice.models.Topic
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface TopicRepository : MongoRepository<Topic, String>
