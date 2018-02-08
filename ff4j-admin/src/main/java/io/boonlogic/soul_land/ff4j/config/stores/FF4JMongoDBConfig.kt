package io.boonlogic.soul_land.ff4j.config.stores

import com.mongodb.MongoClient
import com.mongodb.ServerAddress
import com.mongodb.client.MongoDatabase
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "ff4j.stores.mongodb")
@ConditionalOnClass(MongoClient::class)
class FF4JMongoDBConfig(
        var enabled: Boolean = false,
        val addresses: MutableList<String> = mutableListOf("localhost:27017")) {

    @Bean
    fun mongodb() = MongoClient(mongoServerAddress())

    @Bean
    fun mongodbStore(mongo: MongoClient) = mongo.getDatabase("ff4j")

    private fun mongoServerAddress() = addresses.map {
        val (host,port) = it.split(':')
        ServerAddress(
                host.trim(), port.trim().toIntOrNull() ?: 27017)
    }
}