package io.boonlogic.soul_land.admin

import de.codecentric.boot.admin.config.EnableAdminServer
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Configuration

@Configuration
@EnableAutoConfiguration
@SpringBootApplication
@EnableAdminServer
class AdminServer

fun main(args: Array<String>) {
    SpringApplication.run(AdminServer::class.java, *args)
}

