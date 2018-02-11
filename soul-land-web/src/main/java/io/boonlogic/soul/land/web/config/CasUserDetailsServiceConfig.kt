package io.boonlogic.soul.land.web.config

import org.springframework.context.annotation.Configuration
import com.kakawait.spring.security.cas.userdetails.GrantedAuthoritiesFromAssertionAttributesWithDefaultRolesUserDetailsService
import org.springframework.security.core.authority.SimpleGrantedAuthority
import com.kakawait.spring.boot.security.cas.CasSecurityProperties
import io.boonlogic.soul.land.web.security.cas.CasAssertionAttributeUserDetailsService
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.security.cas.userdetails.AbstractCasAssertionUserDetailsService


@ConditionalOnProperty("security.cas.enabled", havingValue = "true")
@Configuration
class CasUserDetailsServiceConfig {

    @Bean
    fun defaultRolesAuthenticationUserDetailsService(
        casSecurityProperties: CasSecurityProperties): AbstractCasAssertionUserDetailsService {
        val authorities = casSecurityProperties.user.defaultRoles
            .map({ r -> r.removePrefix("ROLE_") })
            .map({ roleName -> SimpleGrantedAuthority("ROLE_$roleName") })
            .toSet()
        val attributes = casSecurityProperties.user.rolesAttributes
        return CasAssertionAttributeUserDetailsService(attributes, authorities)
    }


}