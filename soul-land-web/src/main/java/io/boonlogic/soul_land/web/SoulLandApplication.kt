package io.boonlogic.soul_land

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.netflix.zuul.EnableZuulProxy

@EnableZuulProxy
@SpringBootApplication
class SoulLandApplication

fun main(args: Array<String>) {
    SpringApplication.run(SoulLandApplication::class.java)
}