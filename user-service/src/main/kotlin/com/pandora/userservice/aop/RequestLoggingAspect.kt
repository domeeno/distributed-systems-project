package com.pandora.userservice.aop

import com.pandora.userservice.dto.UserDTO
import com.pandora.userservice.dto.UserLoginDTO
import com.pandora.userservice.exceptions.ApiException
import com.pandora.userservice.models.toPrivate
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.util.UUID

@Aspect
@Component
@Configuration
class RequestLoggingAspect {
    private val logger = LoggerFactory.getLogger("web-request")

    @Around("com.pandora.userservice.aop.Pointcuts.controllerPointcut() && args(.., @RequestBody dto)")
    fun logRequestBody(joinPoint: ProceedingJoinPoint, dto: Any?): Any {
        val requestId = UUID.randomUUID()

        val requestAttr = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes

        val request = requestAttr.request
        val response = requestAttr.response

        val headers = request.headerNames.toList().joinToString(", ") { "\n$it: ${request.getHeader(it)}" }

        val requestURL = request.requestURL

        if (dto != null) {
            logger.info("REQUEST REQUEST-ID: $requestId: ${request.method} $requestURL \nheaders: {$headers\n} \nREQUEST BODY: $dto")
        } else {
            logger.info("REQUEST REQUEST-ID: $requestId: ${request.method} $requestURL \nheaders: {$headers\n}")
        }

        var returnValue: Any

        try {
            returnValue = joinPoint.proceed()
            logger.info("RESPONSE REQUEST-ID: $requestId: ${response?.status} $requestURL \nRESPONSE BODY: $returnValue")
        } catch (e: Exception) {
            logger.error("RESPONSE REQUEST-ID: $requestId: ${response?.status} $requestURL")
            when (e) {
                is ApiException -> throw e
                is DataIntegrityViolationException -> throw ApiException(
                    "Data Integrity exception: " + e.cause?.cause?.message,
                    null,
                    HttpStatus.INTERNAL_SERVER_ERROR
                )
                else -> throw ApiException("Exception " + e.message, null, HttpStatus.INTERNAL_SERVER_ERROR)
            }
        }

        return returnValue
    }

    @Around("com.pandora.userservice.aop.Pointcuts.registerPointcut() && args(.., @RequestBody dto)")
    fun logRequestRegistration(joinPoint: ProceedingJoinPoint, dto: Any?): Any {
        val requestId = UUID.randomUUID()

        val requestAttr = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes

        val request = requestAttr.request
        val response = requestAttr.response

        val headers = request.headerNames.toList().joinToString(", ") { "\n$it: ${request.getHeader(it)}" }

        val requestURL = request.requestURL
        when (dto) {
            is UserLoginDTO -> logger.info("REQUEST REQUEST-ID: $requestId: ${request.method} $requestURL \nheaders: {$headers\n} \nREQUEST BODY: ${dto.toPrivate()}")
            is UserDTO -> logger.info("REQUEST REQUEST-ID: $requestId: ${request.method} $requestURL \nheaders: {$headers\n} \nREQUEST BODY: ${dto.toPrivate()}")
        }

        var returnValue: Any

        try {
            returnValue = joinPoint.proceed()
            logger.info("RESPONSE REQUEST-ID: $requestId: ${response?.status} $requestURL \nRESPONSE BODY: $returnValue")
        } catch (e: Exception) {
            logger.error("RESPONSE REQUEST-ID: $requestId: ${response?.status} $requestURL Exception: {${e.message}}")
            when (e) {
                is ApiException -> throw e
                is DataIntegrityViolationException -> throw ApiException("Data Integrity exception: " + e.cause?.cause?.message, null, HttpStatus.INTERNAL_SERVER_ERROR)
                else -> throw ApiException("Exception " + e.message, null, HttpStatus.INTERNAL_SERVER_ERROR)
            }
        }

        return returnValue
    }
}
