package com.pandora.fileservice.aop

import com.pandora.fileservice.exceptions.ApiException
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.eclipse.jetty.server.Request
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
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

    @Around("com.pandora.fileservice.aop.Pointcuts.controllerPointcut()")
    fun logRequestBody(joinPoint: ProceedingJoinPoint): Any {
        val requestId = UUID.randomUUID()

        val requestAttr = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes

        val request = requestAttr.request
        val response = requestAttr.response

        val headers = request.headerNames.toList().joinToString(", ") { "\n$it: ${request.getHeader(it)}" }

        val originUri = request.requestURL

        logger.info("REQUEST REQUEST-ID: $requestId: ${request.method} $originUri \nheaders: {$headers\n}")

        var returnValue: Any

        try {
            returnValue = joinPoint.proceed()
            logger.info("RESPONSE REQUEST-ID: $requestId: ${response?.status} $originUri")
        } catch (e: Exception) {
            logger.error("RESPONSE REQUEST-ID: $requestId: ${response?.status} $originUri Exception {${e.message}}")
            when (e) {
                is ApiException -> throw e
                else -> throw ApiException("Exception " + e.message, null, HttpStatus.INTERNAL_SERVER_ERROR)
            }
        }

        return returnValue
    }
}
