package com.pandora.courseservice.controllers

import com.pandora.courseservice.dto.TopicDTO
import com.pandora.courseservice.exceptions.ApiException
import com.pandora.courseservice.models.Topic
import com.pandora.courseservice.repository.TopicRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
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
        throw ApiException("Deliberate Error", null, HttpStatus.I_AM_A_TEAPOT)
    }

    @PostMapping("parent/{topicId}")
    fun createTopic(@PathVariable topicId: String, @RequestBody topicDto: TopicDTO): String {
        throw ApiException("Deliberate Error", null, HttpStatus.I_AM_A_TEAPOT)
    }

    @GetMapping
    fun getAllTopics(): List<Topic> {
        throw ApiException("Deliberate Error", null, HttpStatus.I_AM_A_TEAPOT)
    }
}
