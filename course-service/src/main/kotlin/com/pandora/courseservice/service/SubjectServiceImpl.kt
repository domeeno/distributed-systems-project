package com.pandora.courseservice.service

import com.pandora.courseservice.dto.SubjectDTO
import com.pandora.courseservice.dto.SubjectTreeDTO
import com.pandora.courseservice.exceptions.ApiException
import com.pandora.courseservice.models.Subject
import com.pandora.courseservice.models.Topic
import com.pandora.courseservice.repository.GraphLookupRepository
import com.pandora.courseservice.repository.SubjectRepository
import com.pandora.courseservice.repository.TopicRepository
import com.pandora.courseservice.repository.UserSubjectsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

class SubjectServiceImpl(
    @Autowired private val subjectRepository: SubjectRepository,
    @Autowired private val topicRepository: TopicRepository,
    @Autowired private val userSubjectsRepository: UserSubjectsRepository,
    @Autowired private val graphLookupRepository: GraphLookupRepository
) : SubjectService {
    override fun getAllSubjects(): List<Subject> {
        return subjectRepository.findAll()
    }

    override fun getSubjectTree(subjectId: String): SubjectTreeDTO {
        val subject = subjectRepository.findById(subjectId).get()

        // 1: graph lookup for topic

        val tree = graphLookupRepository.getTopicTree(subject.rootTopic)

        // 2: assign to response the subject

        return SubjectTreeDTO(
            subject = subject,
            tree = tree
        )
    }

    override fun createSubject(userSubjectId: String, userId: String, dto: SubjectDTO): String {
        val childTopic = Topic() // Create child topic
        childTopic.topicName = dto.subjectName
        childTopic.userId = userId
        val topic = topicRepository.save(childTopic)

        val subject = Subject() // Create subject
        subject.rootTopic = topic.id // This is the root of the topic tree
        subject.subjectName = dto.subjectName
        subject.description = dto.description
        subject.tags = dto.tags
        subject.userId = userId
        val result = subjectRepository.save(subject)

        val userSubjects = userSubjectsRepository.findById(userSubjectId).get() // update user subject's list
        userSubjects.subjectsList = listOf(result.id) + userSubjects.subjectsList
        userSubjectsRepository.save(userSubjects)

        return result.id
    }

    override fun updateSubject(subjectId: String, dto: SubjectDTO): String {
        val oSubject = subjectRepository.findById(subjectId)

        if(oSubject.isEmpty) {
            throw ApiException("Subject $subjectId not found", null, HttpStatus.NOT_FOUND)
        }

        val subject = oSubject.get()

        subject.subjectName = dto.subjectName
        subject.description = dto.description
        subject.tags = dto.tags
        subject.updateTimestamp = LocalDateTime.now()

        subjectRepository.save(subject)

        return subjectId
    }

    override fun deleteSubject(subjectId: String): String {
        val subject = subjectRepository.findById(subjectId).get()
        val topics = graphLookupRepository.getTopics(subject.rootTopic)

        topicRepository.deleteAll(topics)

        subjectRepository.delete(subject)

        return subjectId
    }
}
