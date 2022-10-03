package com.pandora.courseservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CourseServiceApplication

fun main(args: Array<String>) {
	runApplication<CourseServiceApplication>(*args)
}
