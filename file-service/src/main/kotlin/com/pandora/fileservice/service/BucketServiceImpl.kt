package com.pandora.fileservice.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class BucketServiceImpl: BucketService {

    @Value("\${bucket.rootPath}")
    private val rootPath = ""

    override fun init(): String {
        return rootPath
    }

    override fun store(file: MultipartFile, filename: String): String {
        TODO("Not yet implemented")
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