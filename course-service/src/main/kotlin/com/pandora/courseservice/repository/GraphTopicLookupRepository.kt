package com.pandora.courseservice.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
class GraphTopicLookupRepository(
    @Autowired private val mongoTemplate: MongoTemplate
) {
    fun getSubjectTree(subjectId: String): Optional<String> {
        return Optional.of("Good")
    }
}
