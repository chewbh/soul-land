package io.boonlogic.soul_land.ff4j.config

import org.ff4j.web.FF4jDispatcherServlet
import org.ff4j.FF4j
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.web.servlet.ServletRegistrationBean
import org.springframework.boot.web.support.SpringBootServletInitializer
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.ff4j.web.embedded.ConsoleServlet
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnClass(ConsoleServlet::class, FF4jDispatcherServlet::class)
@AutoConfigureAfter(FF4JConfig::class)
class FF4jServletConfig : SpringBootServletInitializer() {

    @Bean
    fun ff4jDispatcherServletRegistrationBean(ff4jDispatcherServlet: FF4jDispatcherServlet): ServletRegistrationBean {
        return ServletRegistrationBean(ff4jDispatcherServlet, "/ff4j-web-console/*")
    }

    @Bean
    @ConditionalOnMissingBean
    fun getFF4jDispatcherServlet(ff4j: FF4j): FF4jDispatcherServlet {
        val ff4jConsoleServlet = FF4jDispatcherServlet()
        ff4jConsoleServlet.ff4j = ff4j
        return ff4jConsoleServlet
    }
}
