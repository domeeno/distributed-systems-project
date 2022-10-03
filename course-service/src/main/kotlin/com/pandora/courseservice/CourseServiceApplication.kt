package com.pandora.courseservice

import com.pandora.courseservice.repository.LikedRepository
import com.pandora.courseservice.repository.SavedRepository
import com.pandora.courseservice.repository.SubjectRepository
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

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
    runApplication<CourseServiceApplication>(*args)
}
