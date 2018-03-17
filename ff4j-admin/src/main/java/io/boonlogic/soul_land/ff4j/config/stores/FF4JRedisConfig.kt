package io.boonlogic.soul_land.ff4j.config.stores

import org.ff4j.redis.RedisConnection
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import redis.clients.jedis.JedisPool

@Configuration
@ConfigurationProperties(prefix = "ff4j.stores.redis")
class FF4JRedisConfig(
    var enabled: Boolean = false,
    var host: String = "localhost",
    var port: Int = 6379
) {

    @Bean
    fun connection(): RedisConnection { // JedisSentinelPool
        return RedisConnection(JedisPool(host, port))
    }
}
