package com.pandora.courseservice.controllers

import com.pandora.courseservice.dto.TopicDTO
import com.pandora.courseservice.models.Topic
import com.pandora.courseservice.repository.TopicRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/topic")
class TopicController(
    @Autowired private val topicRepository: TopicRepository
) {

    @GetMapping("{topicId}")
    fun getTopic(@PathVariable topicId: String): Topic {
        return topicRepository.findById(topicId).get()
    }

    @PostMapping("parent/{topicId}")
    fun createTopic(@PathVariable topicId: String, @RequestBody topicDto: TopicDTO): String {
        val parentTopic = topicRepository.findById(topicId).get()
        val newTopic = Topic()
        newTopic.topicName = topicDto.topicName
        newTopic.parentId = parentTopic.id
        newTopic.userId = topicDto.userId // TODO Get from token
        val topicDb = topicRepository.save(newTopic)
        parentTopic.childIds = listOf(topicDb.id) + parentTopic.childIds
        topicRepository.save(parentTopic)
        return topicDb.id
    }

    @GetMapping
    fun getAllTopics(): List<Topic> {
        return topicRepository.findAll()
    }
}
