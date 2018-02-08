package io.boonlogic.soul_land.config

import org.ff4j.FF4j
import org.ff4j.core.Feature
import org.ff4j.property.PropertyInt
import org.ff4j.property.PropertyString
import org.ff4j.strategy.el.ExpressionFlipStrategy
import org.ff4j.utils.Util
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.ff4j.web.ApiConfig
import org.ff4j.web.FF4jDispatcherServlet



@Configuration
@ConditionalOnClass(FF4j::class)
@ComponentScan("org.ff4j.spring.boot.web.api",
        "org.ff4j.services",
        "org.ff4j.aop",
        "org.ff4j.spring")
class FF4JConfig(
        @Value("\${ff4j.webapi.authentication}")
        val authentication: Boolean = false,
        @Value("\${ff4j.webapi.authorization}")
        val authorization: Boolean = false) {

    @Bean
    fun getFF4j(): FF4j {
        val ff4j = FF4j("ff4j-features.xml")
                .createFeature("f1")
                .createFeature("AwesomeFeature")
                .createFeature("f2")
                .createFeature("f3")
                .createProperty(PropertyString("SampleProperty", "go!"))
                .createProperty(PropertyInt("SamplePropertyIn", 12))

        val exp = Feature("exp")
        exp.flippingStrategy = ExpressionFlipStrategy(
                "exp", "f1 & f2 | !f1 | f2")
        ff4j.createFeature(exp)
        return ff4j
    }

    @Bean
    fun getFF4JServlet(): FF4jDispatcherServlet {
        val ds = FF4jDispatcherServlet()
        ds.ff4j = getFF4j()
        return ds
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

    // Please add ff4j-store-springjdbc for this sample to work..and a Datasource
    // FF4j ff4j = new FF4j();
    // ff4j.setFeatureStore(new FeatureStoreSpringJdbc(myDataSource));
    // ff4j.setPropertiesStore(new PropertyStoreSpringJdbc(myDataSource));
    // ff4j.setEventRepository(new EventRepositorySpringJdbc(myDataSource));
    // Enable auditing
    // ff4j.audit(true);
    // If feature not found in DB, automatically created (as false)
    // ff4j.autoCreate(enableAutoCreate);
}