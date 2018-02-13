package io.boonlogic.soul.land.web.config

import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class FeatureToggleRestConfig {

    @LoadBalanced
    @Bean
    fun loadBalancedRestTemplate() = RestTemplate()


}