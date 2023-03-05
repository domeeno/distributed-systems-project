package com.pandora.fileservice.service

import com.pandora.fileservice.exceptions.ApiException
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

@Service
class BucketServiceImpl: BucketService {

    @Value("\${bucket.rootPath}")
    private val rootPath = ""

    override fun init(): String {
        return rootPath
    }

    override fun store(file: MultipartFile, userId: String, subjectId: String, topicId: String): String {
        if(file.isEmpty) {
            throw ApiException("file cannot be empty", null, HttpStatus.INTERNAL_SERVER_ERROR)
        }

        val directories = Paths.get(rootPath).resolve("$userId/$subjectId").normalize().toAbsolutePath()

        Files.createDirectories(directories)

        val destinationFile = Paths.get(rootPath).resolve("$userId/$subjectId/$topicId.md").normalize().toAbsolutePath()

        // TODO resolve tomcat deletion exception

        val inputStream = file.inputStream
        Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING)
        return topicId;
    }

    override fun loadResource(filename: String): Resource {
        TODO("Not yet implemented")
    }

    override fun delete(filename: String): String {
        TODO("Not yet implemented")
    }

    override fun update(file: MultipartFile, filename: String): String {
        TODO("Not yet implemented")
    }
}