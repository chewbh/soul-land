package io.boonlogic.support.infra.service.registry

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer

@EnableEurekaServer
@SpringBootApplication
class ServerApplication

fun main(args: Array<String>) {
    SpringApplication.run(ServerApplication::class.java, *args)
}