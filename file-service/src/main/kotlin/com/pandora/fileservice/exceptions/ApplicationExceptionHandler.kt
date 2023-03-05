package com.pandora.fileservice.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.ZonedDateTime
import javax.servlet.http.HttpServletRequest

@ControllerAdvice
class ApplicationExceptionHandler {
    @ExceptionHandler
    fun apiException(
        exception: ApiException,
        req: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            ZonedDateTime.now(),
            exception.status.value(),
            req.requestURI,
            exception.message!!
        )
        return ResponseEntity<ErrorResponse>(error, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler
    fun exceptionHandler(
        ex: Exception,
        req: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            ZonedDateTime.now(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            req.requestURI,
            ex.message!!
        )
        return ResponseEntity<ErrorResponse>(error, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
