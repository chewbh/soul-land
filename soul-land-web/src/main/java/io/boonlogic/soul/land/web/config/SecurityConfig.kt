package io.boonlogic.soul.land.web.config

import com.kakawait.spring.boot.security.cas.CasHttpSecurityConfigurer
import com.kakawait.spring.boot.security.cas.CasSecurityConfigurerAdapter
import com.kakawait.spring.boot.security.cas.CasSecurityProperties
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.core.Ordered
import org.springframework.web.filter.ForwardedHeaderFilter
import org.springframework.boot.autoconfigure.security.Http401AuthenticationEntryPoint
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.logout.LogoutFilter
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.web.util.UriComponentsBuilder


@Configuration
class SecurityConfig: WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity?) {
        if(http != null) {
            http.authorizeRequests()
                .antMatchers("/", "/logout", "/login",
                    "/**/*.css", "/img/**", "/**/*.js",
                    "/rest/**").permitAll()

            CasHttpSecurityConfigurer.cas().configure(http)

            http.antMatcher("/**").authorizeRequests().anyRequest().authenticated()

//            http.exceptionHandling().authenticationEntryPoint(
//                Http401AuthenticationEntryPoint("CAS"))

            println("security configured")
        }
    }

    @Bean
    fun forwardedHeaderFilter(): FilterRegistrationBean {
        val filterRegBean = FilterRegistrationBean()
        filterRegBean.filter = ForwardedHeaderFilter()
        filterRegBean.order = Ordered.HIGHEST_PRECEDENCE
        return filterRegBean
    }
}

@Configuration
class CasLogoutConfig(
    private val casSecurityProperties: CasSecurityProperties) : CasSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity?) {
        if(http != null) {
            // allow GET method to /logout even if CSRF is enabled
            http.logout()
                .permitAll()
                .logoutRequestMatcher(AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/index")

            val ssoLogout = UriComponentsBuilder
                .fromUri(casSecurityProperties.server.baseUrl)
                .path(casSecurityProperties.server.paths.logout)
                .toUriString()

            val filter = LogoutFilter(ssoLogout, SecurityContextLogoutHandler())
            filter.setFilterProcessesUrl("/cas/logout")
            http.addFilterBefore(filter, LogoutFilter::class.java)

            println("logout configured")
        }
    }
}