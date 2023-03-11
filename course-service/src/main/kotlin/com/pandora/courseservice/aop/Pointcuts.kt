package com.pandora.courseservice.aop

import org.aspectj.lang.annotation.Pointcut

class Pointcuts {

    @Pointcut("execution(* com.pandora.courseservice.controllers.*.*(..)) ")
    fun controllerPointcut() {}

    @Pointcut("within(@org.springframework.stereotype.Service *)")
    fun servicePointcut() {}

    @Pointcut("within(@org.springframework.stereotype.Repository *)")
    fun repositoryPointcut() {}
}
