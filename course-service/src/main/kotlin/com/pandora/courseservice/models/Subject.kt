package com.pandora.courseservice.models

import org.springframework.data.mongodb.core.mapping.Document
import javax.persistence.Id

@Document("subjects")
class Subject {
    @Id
    val id = ""

    val subjectName = ""
    val topics: MutableList<Topic> = arrayListOf()
    val createdTimestamp:
}