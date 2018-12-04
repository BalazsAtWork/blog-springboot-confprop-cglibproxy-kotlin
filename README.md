# Spring Boot ConfigurationProperties on Base Classes in Kotlin  

Spring Boot offers an annotation called `@ConfigurationProperties`. It supports the developer with an easy binding of class properties to values contained in a properties file.


## Application-Properties

Lets start with the properties file. If you have created the project with [Spring Initializr][1] then the `application.properties` should already be there in the `/src/main/resources` folder.

Add the following two lines to the file.

```properties
urlconfig.baseUrl=https://github.com
urlconfig.repositoryUrl=reponame
```

## Configuration-Properties
Create a new Kotlin file and name it `ApplicationProperties.kt`.


```kotlin
@Component
@ConfigurationProperties("urlconfig")
class GitHubProperties : BaseUrlProperties() {
    lateinit var repositoryUrl: String
}

abstract class BaseUrlProperties {
    lateinit var baseUrl: String
}
```

## RestController

Now we add a simple RestController class to access the properties via a `REST-Service`

```kotlin
@RestController
class DefaultRestController(private val gitHubProperties: GitHubProperties) {

    @GetMapping("/githuburl")
    fun getGitHubUrl(): String {
        return "${gitHubProperties.baseUrl}/${gitHubProperties.repositoryUrl}"
    }
}
```

That's pretty straight forward!


## Call the endpoint

If calling the endpoint we got the expected result.

```text
http://localhost:8080/githuburl --> https://github.com/reponame
```

You can also execute the provided `ConfigurationPropertiesApplicationTests` class.

## Code continues to evolve

As we continue to develop the codebase we also want to add little more functionality and checks. We want to ensure that the configured repositoryUrl has a minimum length of 4 characters and the baseUrl is a URL.
Because of the we add the `@Validated` annotation on the `ConfigurationProperties` class, add the `@Length` annotation on the `repositoryUrl` property and the `@URL` annotation on the `baseUrl`.

```kotlin
@Component
@ConfigurationProperties("urlconfig")
@Validated
class GitHubProperties : BaseUrlProperties() {
    
    @Length(min = 4)
    lateinit var repositoryUrl: String
}

abstract class BaseUrlProperties {
    
    @URL
    lateinit var baseUrl: String
}
```

Calling the endpoint again, we got an unexpected error from our application.

```text
http://localhost:8080/githuburl --> There was an unexpected error (type=Internal Server Error, status=500).
                                    lateinit property baseUrl has not been initialized
```

If checking the console we can see a `kotlin.UninitializedPropertyAccessException: lateinit property baseUrl has not been initialized`. 

## Analyze

What happened here? It seems that the `baseUrl` is not set any more. But why?
Let's go deeper and debug the code. Set a breakpoint on the return value line of the `getGitHubUrl` method in `DefaultRestController` class.
Start the debugger and call the endpoint again. The debugger stops at the desired line and we can analyze our injected `GitHubProperties` instance.

We can see that the `gitHubProperties` instance is of type `GitHubProperties$$EnhancerBySpringCGLIB$$.....@.....`. It seems that adding the `@Validated` annotation leads to that behavior. Spring wraps our class into a `CglibAopProxy`.
If we decompile the code of `ApplicationProperties` and check what Kotlin is doing with our property `lateinit var baseUrl: String` than we can see that the setter and getter are `final`.

It looks like that the Cglib-Proxy cannot access the baseUrl property because getter and setter are final.

## Solution

The solution to this problem is quite simple.

We only need to `open` the `baseUrl` property and allow the methods to be overwritten.

```kotlin
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
```

If we call the REST-Endpoint again, everything works fine.

A working example of the code used in this post is available on [GitHub][2].


[1]: https://start.spring.io/
[2]: https://github.com/BalazsAtWork/blog-springboot-confprop-cglibproxy-kotlin
