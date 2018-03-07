package io.boonlogic.soul.land.microservices.hello.config

import org.jooq.SQLDialect
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
    fun connectionProvider(appDatasource: DataSource) =
        DataSourceConnectionProvider(appDatasource)

    @Bean
    fun jooqConfiguration(connectionProvider: DataSourceConnectionProvider) =
        DefaultConfiguration().apply {
            set(SQLDialect.POSTGRES)
            set(connectionProvider)
            // set(DefaultExecuteListenerProvider(exception))
        }
}
