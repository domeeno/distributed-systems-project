package com.pandora.userservice.repository

import com.pandora.userservice.dto.UserEntryDTO
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Component
class CourseRepository {
    @Value("\${pandora.services.course-service.url}")
    private val courseUrl: String = ""

    fun createNewUserEntry(userEntryDTO: UserEntryDTO): String {
        val url = UriComponentsBuilder.fromHttpUrl("$courseUrl/user")
            .encode()
            .toUriString()

        val result: ResponseEntity<String> = RestTemplate().postForEntity(url, userEntryDTO, String::class.java)
        return result.body!!
    }

    fun getUserSubjects(userId: String): String? {
        val headers = HttpHeaders()
        headers[HttpHeaders.ACCEPT] = MediaType.APPLICATION_JSON_VALUE
        val entity: HttpEntity<*> = HttpEntity<Any>(headers)

        val urlTemplate = UriComponentsBuilder.fromHttpUrl("$courseUrl/subject")
            .encode()
            .toUriString()

        val params: MutableMap<String, String> = HashMap<String, String>()
        params["userId"] = userId

        val result: ResponseEntity<String> = RestTemplate().exchange(
            urlTemplate,
            HttpMethod.POST,
            entity,
            String::class.java,
            params
        )

        return result.body
    }
}
