package io.boonlogic.soul.land.microservices.hello

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")

@SpringBootApplication
class HelloApplication {

    @GetMapping("hello/{name}")
    fun hello(@PathVariable("name") name: String): String {
        println(name)
        return "hello $name"
    }

}

fun main(args: Array<String>) {
    SpringApplication.run(HelloApplication::class.java, *args)
}