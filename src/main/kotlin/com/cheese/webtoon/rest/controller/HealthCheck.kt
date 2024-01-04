package com.cheese.webtoon.rest.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthCheck {

    @GetMapping("/api/v1/health-check")
    fun healthCheck(): String {
        return "ok"
    }
}