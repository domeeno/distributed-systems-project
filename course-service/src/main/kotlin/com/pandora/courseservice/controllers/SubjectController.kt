package com.pandora.courseservice.controllers

import com.pandora.courseservice.dto.SubjectDTO
import com.pandora.courseservice.dto.SubjectTreeDTO
import com.pandora.courseservice.models.Subject
import com.pandora.courseservice.service.SubjectService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("subject")
class SubjectController(
    @Autowired private val subjectService: SubjectService,
) {

    @GetMapping
    fun getAllSubjects(): List<Subject> {
        return subjectService.getAllSubjects()
    }

    @GetMapping("{subjectId}")
    fun getSubjectTree(@PathVariable subjectId: String): SubjectTreeDTO {
        return subjectService.getSubjectTree(subjectId)
    }

    @PostMapping("{userSubjectId}/user/{userId}")
    fun createSubject(
        @PathVariable userSubjectId: String,
        @PathVariable userId: String,
        @RequestBody dto: SubjectDTO
    ): String {
        return subjectService.createSubject(userSubjectId, userId, dto)
    }

    @PutMapping("{subjectId}")
    fun updateSubject(
        @PathVariable subjectId: String,
        @RequestBody dto: SubjectDTO
    ): String {
        return subjectService.updateSubject(subjectId, dto)
    }

    @DeleteMapping("{subjectId}")
    fun deleteSubject(@PathVariable subjectId: String): String {
        return subjectService.deleteSubject(subjectId)
    }
}
