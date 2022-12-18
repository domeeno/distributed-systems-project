package com.pandora.courseservice.controllers

import com.pandora.courseservice.dto.CreateSubjectDTO
import com.pandora.courseservice.models.Subject
import com.pandora.courseservice.models.Topic
import com.pandora.courseservice.repository.GraphTopicLookupRepository
import com.pandora.courseservice.repository.SubjectRepository
import com.pandora.courseservice.repository.TopicRepository
import com.pandora.courseservice.repository.UserSubjectsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("subject")
class SubjectController(
    @Autowired private val graphTopicLookupRepository: GraphTopicLookupRepository,
    @Autowired private val subjectRepository: SubjectRepository,
    @Autowired private val topicRepository: TopicRepository,
    @Autowired private val userSubjectsRepository: UserSubjectsRepository
) {

    @GetMapping
    fun getAllSubjects(): ResponseEntity<List<Subject>> {
        return ResponseEntity.ok(subjectRepository.findAll())
    }

    @GetMapping("{subjectId}")
    fun getSubjectTree(@PathVariable subjectId: String): ResponseEntity<String> {
        val subject = subjectRepository.findById(subjectId).get()

        return ResponseEntity.ok(subject.id)
    }

    @PostMapping("{userSubjectId}/user/{userId}")
    fun createSubject(
        @PathVariable userSubjectId: String,
        @PathVariable userId: String,
        @RequestBody subjectDto: CreateSubjectDTO
    ): ResponseEntity<String> {

        val childTopic = Topic() // Create child topic
        childTopic.topicName = subjectDto.subjectName
        childTopic.userId = userId
        val topic = topicRepository.save(childTopic)

        val subject = Subject() // Create subject
        subject.rootTopic = topic.id // This is the root of the topic tree
        subject.subjectName = subjectDto.subjectName
        subject.description = subjectDto.description
        subject.tags = subjectDto.tags
        subject.userId = userId
        val result = subjectRepository.save(subject)

        val userSubjects = userSubjectsRepository.findById(userSubjectId).get() // update user subject's list
        userSubjects.subjectsList = listOf(result.id) + userSubjects.subjectsList
        userSubjectsRepository.save(userSubjects)

        return ResponseEntity.ok(result.id)
    }

    @PutMapping("{subjectId}")
    fun updateSubject() {
        TODO()
    }

    @DeleteMapping("{subjectId}")
    fun deleteSubject() {
        TODO()
    }
}
