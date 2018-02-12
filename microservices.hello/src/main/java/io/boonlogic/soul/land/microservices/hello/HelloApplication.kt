package io.boonlogic.soul.land.microservices.hello

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.sql.DataSource

@RestController
@RequestMapping("/api")

@SpringBootApplication
class HelloApplication(
    private val ds: DataSource
) {

    @GetMapping("hello/{name}")
    fun hello(@PathVariable("name") name: String): String {
        println(name)
        return "hello $name"
    }

    @GetMapping("hellodb/{name}")
    fun helloDB(@PathVariable("name") name: String): String {
        println(name)
        println(ds.connection.autoCommit)
        return "hello $name"
    }

}

fun main(args: Array<String>) {
    SpringApplication.run(HelloApplication::class.java, *args)
}