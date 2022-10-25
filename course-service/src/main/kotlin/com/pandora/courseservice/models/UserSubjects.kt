package com.pandora.courseservice.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("user_subjects")
class UserSubjects {
    @Id
    var id = ""

    var subjectsList: List<String> = arrayListOf()
}
