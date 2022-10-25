package com.pandora.courseservice.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Field
import java.sql.Timestamp

class Topic {

    @Id
    var id = ""

    @Field(name = "parent_id")
    var parentId = ""

    @Field(name = "user_id")
    var userId = ""

    @Field(name = "topic_name")
    var topicName = ""

    var topics: MutableList<Topic> = arrayListOf()

    @Field(name = "file_id")
    var fileId = ""

    @Field(name = "create_timestamp")
    var createTimestamp: Timestamp = Timestamp(System.currentTimeMillis())

    @Field(name = "update_timestamp")
    var updateTimestamp: Timestamp = Timestamp(System.currentTimeMillis())
}
