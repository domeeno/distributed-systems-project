package com.pandora.courseservice.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("subject")
class SubjectController {


    @GetMapping("/{subjectId}")
    fun getSubject() {
        TODO()
    }

    @PostMapping("/{subjectId}")
    fun createSubject() {
        TODO()
    }

    @PutMapping("/{subjectId}")
    fun updateSubject() {
        TODO()
    }

    @DeleteMapping("/{subjectId}")
    fun deleteSubject() {
        TODO()
    }
}
