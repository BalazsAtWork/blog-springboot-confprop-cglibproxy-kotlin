package com.blog.configurationproperties

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ConfigurationPropertiesApplication

fun main(args: Array<String>) {
    runApplication<ConfigurationPropertiesApplication>(*args)
}
