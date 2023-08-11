package com.backend.template

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.backend.template"])
open class BackendTemplateApplication

fun main(args: Array<String>) {
    runApplication<BackendTemplateApplication>(*args)
}
