package com.pandora.userservice.aop

import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component

@Component
class Pointcuts {

    @Pointcut(
        "execution(* com.pandora.userservice.controllers.*.*(..)) " +
            "&& !execution(* com.pandora.userservice.controllers.RegisterController.*(..))"
    )
    fun controllerPointcut() {}

    @Pointcut("execution(* com.pandora.userservice.controllers.RegisterController.*(..))")
    fun registerPointcut() {}

    @Pointcut("within(@org.springframework.stereotype.Service *)")
    fun servicePointcut() {}

    @Pointcut("within(@org.springframework.stereotype.Repository *)")
    fun repositoryPointcut() {}
}
