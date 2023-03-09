package com.pandora.userservice.aop

import org.aspectj.lang.annotation.Pointcut

class Pointcuts {

    @Pointcut("within(@org.springframework.stereotype.Controller *)")
    fun controllerPointcut() {}

    @Pointcut("within(@org.springframework.stereotype.Service *)")
    fun servicePointcut() {}

    @Pointcut("within(@org.springframework.stereotype.Repository *)")
    fun repositoryPointcut() {}
}
