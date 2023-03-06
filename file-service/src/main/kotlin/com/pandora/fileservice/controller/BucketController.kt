package com.pandora.fileservice.controller

import com.pandora.fileservice.service.BucketService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("bucket")
class BucketController(
    @Autowired private val bucketService: BucketService
) {

    @GetMapping("init")
    fun init(): String {
        return bucketService.init()
    }

    @PostMapping(
        path = ["{userId}/{subjectId}/{topicId}"],
        consumes = [
            MediaType.TEXT_MARKDOWN_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE,
//            MediaType.APPLICATION_JSON_VALUE
        ]
    )
    fun createFile(
        @PathVariable userId: String,
        @PathVariable subjectId: String,
        @PathVariable topicId: String,
        @RequestPart("file") file: MultipartFile
    ): String {
        return bucketService.store(file, userId, subjectId, topicId)
    }

    @GetMapping("{userId}/{subjectId}/{topicId}")
    fun getFile(
        @PathVariable userId: String,
        @PathVariable subjectId: String,
        @PathVariable topicId: String
    ): Resource {
        return bucketService.loadResource("$userId/$subjectId/$topicId.md")
    }

    @DeleteMapping("{userId}/{subjectId}/{topicId}")
    fun deleteFile(
        @PathVariable userId: String,
        @PathVariable subjectId: String,
        @PathVariable topicId: String
    ): String {
        return bucketService.delete("$userId/$subjectId/$topicId.md")
    }

    @DeleteMapping("{userId}/{subjectId}")
    fun deleteSubject(
        @PathVariable userId: String,
        @PathVariable subjectId: String
    ): String {
        return bucketService.deleteSubject("$userId/$subjectId")
    }
}
