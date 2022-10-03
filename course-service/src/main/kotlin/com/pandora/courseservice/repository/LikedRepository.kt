package com.pandora.courseservice.repository

import com.pandora.courseservice.models.Liked
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface LikedRepository : MongoRepository<Liked, String>
