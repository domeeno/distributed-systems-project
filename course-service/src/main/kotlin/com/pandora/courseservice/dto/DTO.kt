package com.pandora.courseservice.dto

import com.pandora.courseservice.models.Subject
import com.pandora.courseservice.models.Topic

data class EntryDTO(
    val likedId: String,
    val savedId: String,
    val subjectsId: String
)

data class LikedListDTO(
    val userLikes: List<SubjectDTO>
)

data class SavedListDTO(
    val userSaves: List<SubjectDTO>
)

data class SubjectListDTO(
    val userSubjects: List<SubjectDTO>
)

data class SubjectDTO(
    val subjectId: String,
    val subjectName: String
)

data class CreateSubjectDTO(
    val subjectName: String,
    val description: String,
    val tags: List<String>
)

data class TopicDTO(
    val userId: String, // Get from token
    val topicName: String
)

data class SubjectTreeDTO(
    val subject: Subject,
    val tree: Topic
)
