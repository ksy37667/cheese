package com.cheese

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@EnableWebMvc
@SpringBootApplication
class CheeseApplication

fun main(args: Array<String>) {
    runApplication<CheeseApplication>(*args)
}
