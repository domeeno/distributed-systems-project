package com.pandora.courseservice.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.sql.Timestamp
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

    var topics: MutableList<Topic> = arrayListOf()

    @Field(name = "document_id")
    var documentId: String? = null

    @Field(name = "create_timestamp")
    var createTimestamp: Timestamp = Timestamp(System.currentTimeMillis())

    @Field(name = "update_timestamp")
    var updateTimestamp: Timestamp = Timestamp(System.currentTimeMillis())
}
