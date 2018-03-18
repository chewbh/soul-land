package io.boonlogic.soul_land.ff4j.config

import org.ff4j.web.ApiConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var apiConfig: ApiConfig

    override fun configure(auth: AuthenticationManagerBuilder?) {
//        super.configure(auth)
        // Load APIConfig Users intoSpring security in Memory ...
        if (auth != null) {
            var config = auth.inMemoryAuthentication()
            if (apiConfig.isAuthenticate) {
                val count = apiConfig.users.keys.size // .users.keySet().size()
                var idx = 0
                for (currentUser in apiConfig.users.keys) {
                    val udb =
                            config.withUser(currentUser)
                            .password(apiConfig.users[currentUser])
                            .roles(apiConfig.permissions[currentUser]!!.toTypedArray()[0])
                    // There is another user to use
                    if (idx++ < count) {
                        config = udb.and()
                    }
                }
            }
        }
    }

    override fun configure(http: HttpSecurity?) {
//        super.configure(http)
        if (http != null) {
            if (apiConfig.isAuthenticate()) {
                // ENFORCE AUTHENTICATION
                http.httpBasic()
                        .// DISABLE CSRF
                        and().csrf().disable()
                        .authorizeRequests()
                        .antMatchers("/ff4j-web-console/**").hasRole("ADMIN")
                        .antMatchers("/api/**").hasRole("USER")
                        .antMatchers("/").permitAll()
                        .anyRequest().authenticated()
            } else {
                http.csrf().disable()
            }
        }
    }
}
