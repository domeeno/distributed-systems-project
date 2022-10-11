package com.pandora.courseservice.dto

data class EntryDTO(
    val likedId: String,
    val savedId: String
)

data class LikedListDTO(
    val userLikes: List<LikedDTO>
)

data class SavedListDTO(
    val userSaves: List<SavedDTO>
)

data class LikedDTO(
    val subjectId: String,
    val subjectName: String
)

data class SavedDTO(
    val subjectId: String,
    val subjectName: String
)
