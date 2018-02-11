package io.boonlogic.soul.land.web.config

import org.ff4j.FF4j
import org.ff4j.web.jersey2.store.FeatureStoreHttp
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.ff4j.redis.RedisConnection
import org.springframework.beans.factory.annotation.Autowired

@Configuration
@ConditionalOnClass(FF4j::class)
class FF4JConfig {
    @Autowired
    lateinit var redisConnection: RedisConnection

    @Bean
    fun getFF4j(): FF4j {


        val ff4j = FF4j().apply {
            featureStore = FeatureStoreHttp("http://localhost:6002/api/ff4j")

            // Enable Cache Proxy
//            val redisCache = FF4jCacheManagerRedis(redisConnection)
//            redisCache.timeToLive = 3600 * 2
            // ff4j.cache(InMemoryCacheManager())
//            cache(redisCache)

            // auto create if not exist but set default to false
            autoCreate(true)
        }

        return ff4j
    }

}