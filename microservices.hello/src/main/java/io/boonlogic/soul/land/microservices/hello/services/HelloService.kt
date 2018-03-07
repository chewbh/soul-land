package io.boonlogic.soul.land.microservices.hello.services

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import javax.sql.DataSource


@RestController
class HelloService(

) {
    @GetMapping("hello/{name}")
    fun hello(@PathVariable("name") name: String): String {
        println(name)
        return "hello $name"
    }

//    @GetMapping("hellodb/{name}")
//    fun helloDB(@PathVariable("name") name: String): String {
//        println(name)
//        println(ds.connection.autoCommit)
//        return "hello $name"
//    }
}
