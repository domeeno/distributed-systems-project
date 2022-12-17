package com.pandora.courseservice.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.LocalDateTime
import java.util.UUID

@Document("topics")
class Topic {

    /*
       This Document stores Subject's topics, in other words this will branch out the subject into multiple topics
       They exist attached to a subject.
    */

    @Id
    var id = UUID.randomUUID().toString()

    @Field(name = "parent_id")
    var parentId: String? = null

    @Field(name = "user_id")
    var userId = ""

    @Field(name = "topic_name")
    var topicName = ""

    var childTopics: List<String> = arrayListOf()

    @Field(name = "document_id")
    var documentId: String? = null

    @Field(name = "create_timestamp")
    var createTimestamp: LocalDateTime = LocalDateTime.now()

    @Field(name = "update_timestamp")
    var updateTimestamp: LocalDateTime = LocalDateTime.now()
}
