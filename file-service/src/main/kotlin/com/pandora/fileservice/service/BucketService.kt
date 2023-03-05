package com.pandora.fileservice.service

import org.springframework.core.io.Resource
import org.springframework.web.multipart.MultipartFile

interface BucketService {

    fun init(): String

    fun store(file: MultipartFile, userId: String, subjectId: String, topicId: String): String

    fun loadResource(filename: String): Resource

    fun delete(filename: String): String
}
