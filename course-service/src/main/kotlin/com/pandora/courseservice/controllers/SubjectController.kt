package com.pandora.courseservice.controllers

import com.pandora.courseservice.dto.SubjectDTO
import com.pandora.courseservice.dto.SubjectSearchDTO
import com.pandora.courseservice.dto.SubjectTreeDTO
import com.pandora.courseservice.exceptions.ApiException
import com.pandora.courseservice.models.Subject
import com.pandora.courseservice.service.SubjectService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("subject")
class SubjectController(
    @Autowired private val subjectService: SubjectService,
) {

    @GetMapping
    fun getAllSubjects(): List<Subject> {
        throw ApiException("Deliberate Error", null, HttpStatus.I_AM_A_TEAPOT)
    }

    @GetMapping("search")
    fun getSubjects(
        @RequestParam page: Int,
        @RequestParam size: Int,
        @RequestParam input: String?
    ): List<SubjectSearchDTO> {
        throw ApiException("Deliberate Error", null, HttpStatus.I_AM_A_TEAPOT)
    }

    @GetMapping("{subjectId}")
    fun getSubjectTree(@PathVariable subjectId: String): SubjectTreeDTO {
        throw ApiException("Deliberate Error", null, HttpStatus.I_AM_A_TEAPOT)
    }

    @PostMapping("{userSubjectId}/user/{userId}")
    fun createSubject(
        @PathVariable userSubjectId: String,
        @PathVariable userId: String,
        @RequestBody dto: SubjectDTO
    ): String {
        throw ApiException("Deliberate Error", null, HttpStatus.I_AM_A_TEAPOT)
    }

    @PutMapping("{subjectId}")
    fun updateSubject(
        @PathVariable subjectId: String,
        @RequestBody dto: SubjectDTO
    ): String {
        throw ApiException("Deliberate Error", null, HttpStatus.I_AM_A_TEAPOT)
    }

    @DeleteMapping("{subjectId}")
    fun deleteSubject(@PathVariable subjectId: String): String {
        throw ApiException("Deliberate Error", null, HttpStatus.I_AM_A_TEAPOT)
    }
}
