package com.pandora.courseservice.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("user_subjects")
class UserSubjects (
    /*
        This Document stores user created subjects
    */

    @Id
    var id: String = "",

    var subjectsList: List<String> = arrayListOf()
)
