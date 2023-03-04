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

    @PostMapping("{filename}",
        consumes = [
            MediaType.TEXT_MARKDOWN_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_JSON_VALUE
        ])
    fun createFile(@RequestPart("file") file: MultipartFile, @PathVariable filename: String): String {
        return bucketService.store(file, filename)
    }

    @GetMapping("{filename}")
    fun getFile(@PathVariable filename: String): Resource {
        return bucketService.loadResource(filename)
    }

    @DeleteMapping("{filename}")
    fun deleteFile(@PathVariable filename: String): String {
        return bucketService.delete(filename)
    }

    @PostMapping("{filename}",
        consumes = [
            MediaType.TEXT_MARKDOWN_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_JSON_VALUE
        ])
    fun updateFile(@RequestPart file: MultipartFile, @PathVariable filename: String): String {
        return bucketService.update(file, filename)
    }
}