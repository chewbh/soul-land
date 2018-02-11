package io.boonlogic.soul.land.web

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.netflix.zuul.EnableZuulProxy
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity

@EnableZuulProxy
@SpringBootApplication
@EnableGlobalMethodSecurity(securedEnabled = true)
class SoulLandApplication

fun main(args: Array<String>) {
    SpringApplication.run(SoulLandApplication::class.java)
}