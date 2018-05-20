package io.boonlogic.soul.land.microservices.registration

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

//@EnableDiscoveryClient
//@EnableHypermediaSupport(type = {HypermediaType.HAL})
@SpringBootApplication
class Application

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}
