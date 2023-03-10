package com.pandora.courseservice.exceptions

import org.springframework.http.HttpStatus

class ApiException(
    override val message: String?,
    override val cause: Throwable?,
    val status: HttpStatus
) : java.lang.RuntimeException()
