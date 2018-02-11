package io.boonlogic.soul.land.web.security.cas

import org.jasig.cas.client.validation.Assertion
import org.springframework.security.cas.userdetails.AbstractCasAssertionUserDetailsService
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.util.StringUtils
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream

private const val NON_EXISTENT_PASSWORD_VALUE = "NO_PASSWORD"

class CasAssertionAttributeUserDetailsService(
    val attributes: Array<String>,
    val defaultGrantedAuthorities: Collection<GrantedAuthority>) :
    AbstractCasAssertionUserDetailsService() {

    private val toUppercase = true

    override fun loadUserDetails(assertion: Assertion?): UserDetails {

        if (assertion != null) {
            val username = assertion.principal.name
            if (username.isNullOrBlank()) throw UsernameNotFoundException("Unable to retrieve username from CAS assertion")

            val authorities = attributes
                .mapNotNull { assertion.principal.attributes[it] }
                .flatMap { value -> if (value is Collection<*>) value.toList() else listOf(value) }
                .map { if (toUppercase) it.toString().toUpperCase() else it.toString() }
                .map { it.dnToGroupName() }
                .map { it.removePrefix("ROLE_") }
                .map { rolename -> SimpleGrantedAuthority("ROLE_$rolename") }
                .toList()

            return User(username, NON_EXISTENT_PASSWORD_VALUE, authorities + defaultGrantedAuthorities)
        }
        throw IllegalStateException("CAS Assertion should not be null")
    }
}

fun String.dnToGroupName() =
    if(this.contains(','))
        this.split(',').first().removePrefix("CN=").trim()
    else
        this.removePrefix("CN=").trim()