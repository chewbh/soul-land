package io.boonlogic.soul_land.ff4j.config

import com.mongodb.client.MongoDatabase
import io.boonlogic.soul_land.ff4j.config.stores.FF4JMongoDBConfig
import io.boonlogic.soul_land.ff4j.config.stores.FF4JRedisConfig
import org.ff4j.FF4j
import org.ff4j.mongo.store.EventRepositoryMongo
import org.ff4j.mongo.store.FeatureStoreMongo
import org.ff4j.mongo.store.PropertyStoreMongo
import org.ff4j.redis.RedisConnection
import org.ff4j.store.EventRepositoryRedis
import org.ff4j.store.FeatureStoreRedis
import org.ff4j.store.PropertyStoreRedis
import org.ff4j.utils.Util
import org.ff4j.web.ApiConfig
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.beans.factory.annotation.Autowired

@Configuration
@ConditionalOnClass(FF4j::class)
@ComponentScan("org.ff4j.spring.boot.web.api",
        "org.ff4j.services", "org.ff4j.aop", "org.ff4j.spring")
class FF4JConfig(
    @Value("\${ff4j.webapi.authentication}") val authentication: Boolean = false,
    @Value("\${ff4j.webapi.authorization}") val authorization: Boolean = false
) {

    val log = LoggerFactory.getLogger(FF4JConfig::class.java)

    @Autowired(required = false) private var mongoStore: MongoDatabase? = null
    @Autowired private lateinit var mongoConfig: FF4JMongoDBConfig

    @Autowired(required = false) private var redisStore: RedisConnection? = null
    @Autowired private lateinit var redisConfig: FF4JRedisConfig

            @Bean
    fun getFF4j(): FF4j {

        val ff4j = FF4j().apply {
            if (mongoConfig.enabled && mongoStore != null) {
                featureStore = FeatureStoreMongo(mongoStore, "features")
                propertiesStore = PropertyStoreMongo(mongoStore, "properties")
                eventRepository = EventRepositoryMongo(mongoStore, "events")
                log.info("configuring ff4j stores backed by MongoDB={}", mongoStore?.name)
            } else if (redisConfig.enabled && redisStore != null) {
                featureStore = FeatureStoreRedis(redisStore)
                propertiesStore = PropertyStoreRedis(redisStore)
                eventRepository = EventRepositoryRedis(redisStore)
                log.info("configuring ff4j stores backed by Redis={}", redisStore?.redisHost)
            } else {
                log.info("configuring ff4j stores failed")
            }

            // if feature not found in DB, automatically created (as false)
            autoCreate(true)

            // enable auditing
            audit(true)
        }

        return ff4j
    }

    @Bean
    fun getApiConfig(): ApiConfig {
        val apiConfig = ApiConfig()

        // Enable Authentication on API KEY
        apiConfig.isAuthenticate = false
        apiConfig.createApiKey("apikey1", true, true, Util.set("ADMIN", "USER"))
        apiConfig.createApiKey("apikey2", true, true, Util.set("ADMIN", "USER"))
        apiConfig.createUser("userName", "password", true, true, Util.set("ADMIN", "USER"))
        apiConfig.createUser("user", "userPass", true, true, Util.set("ADMIN", "USER"))
        apiConfig.createUser("a", "a", true, true, Util.set("ADMIN", "USER"))
        apiConfig.createUser("b", "b", true, true, Util.set("ADMIN", "USER"))

        // Check Autorization as well
        apiConfig.isAutorize = false
        apiConfig.webContext = "/api"
        apiConfig.port = 6001
        apiConfig.fF4j = getFF4j()
        return apiConfig
    }
}
