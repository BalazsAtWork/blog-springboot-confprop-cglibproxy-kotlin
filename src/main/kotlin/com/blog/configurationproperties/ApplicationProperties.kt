package com.blog.configurationproperties

import org.hibernate.validator.constraints.Length
import org.hibernate.validator.constraints.URL
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated

@Component
@ConfigurationProperties("urlconfig")
@Validated
class GitHubProperties : BaseUrlProperties() {

    @Length(min = 4)
    lateinit var repositoryUrl: String
}

abstract class BaseUrlProperties {

    @URL
    open lateinit var baseUrl: String
}
