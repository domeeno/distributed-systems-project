package com.pandora.courseservice.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.sql.Timestamp

@Document("subjects")
class Subject {
    @Id
    var id = ""

    @Field(name = "subject_name")
    var subjectName = ""

    @Field(name = "user_id")
    var userId = ""

    var topics: MutableList<Topic> = arrayListOf()

    var likes: Int = 0

    var saves: Int = 0

    @Field(name = "document_id")
    var documentId = ""

    @Field(name = "create_timestamp")
    var createTimestamp: Timestamp = Timestamp(System.currentTimeMillis())

    @Field(name = "update_timestamp")
    var updateTimestamp: Timestamp = Timestamp(System.currentTimeMillis())
}
