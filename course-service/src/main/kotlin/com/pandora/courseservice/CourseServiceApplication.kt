package com.pandora.courseservice

import com.pandora.courseservice.repository.LikedRepository
import com.pandora.courseservice.repository.SavedRepository
import com.pandora.courseservice.repository.SubjectRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import java.net.InetAddress

@SpringBootApplication(scanBasePackages = ["com.pandora.courseservice"])
@EnableMongoRepositories(
    basePackageClasses = [
        LikedRepository::class,
        SavedRepository::class,
        SubjectRepository::class,
        MongoTemplate::class
    ]
)
class CourseServiceApplication


fun main(args: Array<String>) {
    val log: Logger = LoggerFactory.getLogger(CourseServiceApplication::class.java)
    val context = runApplication<CourseServiceApplication>(*args)

    log.info("Local: http://127.0.0.1:{}",
        context.environment.getProperty("server.port", "8082")
    )

    log.info("External: http://{}:{}",
        InetAddress.getLocalHost().hostAddress,
        context.environment.getProperty("server.port", "8082")
    )
}
