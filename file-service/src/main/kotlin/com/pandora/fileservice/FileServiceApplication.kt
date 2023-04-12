package com.pandora.fileservice

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.net.InetAddress

@SpringBootApplication
class FileServiceApplication

fun main(args: Array<String>) {
    val log: Logger = LoggerFactory.getLogger(FileServiceApplication::class.java)
    val context = runApplication<FileServiceApplication>(*args)

    log.info(
        "External: http://{}:{}",
        InetAddress.getLoopbackAddress().hostName,
        context.environment.getProperty("server.port", "8080")
    )
}
