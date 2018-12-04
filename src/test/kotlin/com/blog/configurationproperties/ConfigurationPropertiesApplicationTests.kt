package com.blog.configurationproperties

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@AutoConfigureMockMvc
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = MOCK)
class ConfigurationPropertiesApplicationTests {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    @Throws(Exception::class)
    fun gitHubUrlIsReturned() {
        mockMvc.perform(get("/githuburl"))
                .andExpect(status().is2xxSuccessful)
                .andExpect(content().string("https://github.com/reponame"))
    }
}
