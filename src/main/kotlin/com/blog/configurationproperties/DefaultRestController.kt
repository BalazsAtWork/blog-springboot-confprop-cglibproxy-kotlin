package com.blog.configurationproperties

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class DefaultRestController(private val gitHubProperties: GitHubProperties) {

    @GetMapping("/githuburl")
    fun getGitHubUrl(): String {
        return "${gitHubProperties.baseUrl}/${gitHubProperties.repositoryUrl}"
    }
}
