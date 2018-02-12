package io.boonlogic.soul.land.microservices.hello.config

import org.jooq.impl.DataSourceConnectionProvider
import org.jooq.impl.DefaultConfiguration
import org.jooq.impl.DefaultExecuteListener
import org.jooq.impl.DefaultExecuteListenerProvider
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class DatabaseConfig {

    @Bean
    @ConfigurationProperties("datasource.app")
    fun appDatasource() = DataSourceBuilder.create().build()

    @Bean
    fun connectionProvider(ds: DataSource) = DataSourceConnectionProvider(ds)

    fun jooqConfiguration(connectionProvider: DataSourceConnectionProvider) {
        val jooqConfig = DefaultConfiguration()
        jooqConfig.set(connectionProvider)
//        jooqConfig.set(DefaultExecuteListenerProvider(exception))
    }

}