package com.pandora.courseservice.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.LocalDateTime
import java.util.UUID

@Document("subjects")
class Subject(
    /*
       This Document stores SUBJECTS - in other words the root of the Subject is this.
       It can have multiple TOPICS

       To create a topic you have to have a Subject
    */

    @Id
    var id: String = UUID.randomUUID().toString(),

    @Field(name = "subject_name")
    var subjectName: String = "",

    @Field(name = "user_id")
    var userId: String = "",

    var description: String? = "",

    // subject root topic
    @Field(name = "root_topic")
    var rootTopic: String = "",

    var tags: List<String> = arrayListOf(),

    var likes: Int = 0,

    var saves: Int = 0,

//    @Field(name = "document_id")
//    var documentId = ""

    @Field(name = "create_timestamp")
    var createTimestamp: LocalDateTime = LocalDateTime.now(),

    @Field(name = "update_timestamp")
    var updateTimestamp: LocalDateTime = LocalDateTime.now()
)
