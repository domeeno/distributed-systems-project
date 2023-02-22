package com.pandora.courseservice.controllers

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping("service")
class DiscoveryController {

    @PostMapping("/{service}")
    fun discover(@PathVariable service: String, @RequestParam port: String, request: HttpServletRequest): ResponseEntity<String> {
        println(request.remoteAddr)
        println(service)
        return ResponseEntity.ok("Good")
    }
}
