package com.pandora.userservice.repository

import com.pandora.userservice.dto.UserEntryDTO
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Component
class CourseRepository {
    @Value("\${pandora.services.course-service.url}")
    private val courseUrl: String = ""

    private val log = LoggerFactory.getLogger(CourseRepository::class.java)

    fun createNewUserEntry(userEntryDTO: UserEntryDTO): String {

        log.info("Sending create request to: $courseUrl/user/new")

        val url = UriComponentsBuilder.fromHttpUrl("$courseUrl/user/new")
            .encode()
            .toUriString()

        val result: ResponseEntity<String> = RestTemplate().postForEntity(url, userEntryDTO, String::class.java)
        return result.body!!
    }

    fun deleteUserEntries(userEntryDTO: UserEntryDTO): String {
        log.info("Sending delete request to: $courseUrl/user")

        val url = UriComponentsBuilder.fromHttpUrl("$courseUrl/user")
            .encode()
            .toUriString()

        val params: MutableMap<String, String> = HashMap()
        params["likedId"] = userEntryDTO.likedId.toString()
        params["savedId"] = userEntryDTO.savedId.toString()
        params["subjectId"] = userEntryDTO.subjectsId.toString()

        val headers = HttpHeaders()
        headers.set("Accept", "application/json")

        val entity: HttpEntity<*> = HttpEntity<Any>(headers)

        val result: ResponseEntity<String> = RestTemplate().exchange(url, HttpMethod.DELETE, entity, String::class.java, params)
        return result.body!!
    }
}
