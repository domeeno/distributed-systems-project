package com.pandora.userservice.aop

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration

@Aspect
@Configuration
class RequestLoggingAspect {
    private val logger = LoggerFactory.getLogger("web-request")

    @Around("com.pandora.userservice.aop.Pointcuts.controllerPointcut()")
    fun logRequest(joinPoint: ProceedingJoinPoint) {
        logger.info("Receiving request: ")
        val returnValue = joinPoint.proceed()
        logger.info("Done")
    }
}
