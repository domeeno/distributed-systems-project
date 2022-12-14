package com.pandora.courseservice.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("user_likes")
class Liked {
    /*
        This Document stores user liked subjects
    */

    @Id
    var id = ""

    var likedList: List<String> = arrayListOf()
}
