package com.pandora.courseservice.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.sql.Timestamp

@Document("subjects")
class Subject {
    /*
       This Document stores SUBJECTS - in other words the root of the Subject is this.
       It can have multiple TOPICS

       To create a topic you have to have a Subject
    */

    @Id
    var id = ""

    @Field(name = "subject_name")
    var subjectName = ""

    @Field(name = "user_id")
    var userId = ""

    var topics: MutableList<Topic> = arrayListOf()

    var tags: MutableList<String> = arrayListOf()

    var likes: Int = 0

    var saves: Int = 0

    @Field(name = "document_id")
    var documentId = ""

    @Field(name = "create_timestamp")
    var createTimestamp: Timestamp = Timestamp(System.currentTimeMillis())

    @Field(name = "update_timestamp")
    var updateTimestamp: Timestamp = Timestamp(System.currentTimeMillis())
}
