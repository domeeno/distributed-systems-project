package com.pandora.courseservice.extensions

import com.pandora.courseservice.dto.SubjectSearchDTO
import com.pandora.courseservice.models.Liked
import com.pandora.courseservice.models.Saved
import com.pandora.courseservice.models.Subject

fun Liked.toSubjectIdList(): List<Subject> {
    return this.likedList.map {
        val subject = Subject()
        subject.id = it
        subject
    }
}

fun Saved.toSubjectIdList(): List<Subject> {
    return this.savedList.map {
        val subject = Subject()
        subject.id = it
        subject
    }
}

fun Subject.toSubjectSearchDTO(): SubjectSearchDTO {
    return SubjectSearchDTO(
        subjectId = id,
        subjectName = subjectName,
        description = description,
        tags = tags
    )
}
