package com.pandora.courseservice.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("user_saves")
class Saved (
    /*
        This Document stores user saved subjects
    */

    @Id
    var id: String = "",

    var savedList: List<String> = arrayListOf()
)
