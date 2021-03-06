package io.boonlogic.soul.land.web.config

import org.ff4j.core.Feature
import org.ff4j.exception.FeatureAccessException
import org.ff4j.exception.FeatureAlreadyExistException
import org.ff4j.exception.FeatureNotFoundException
import org.ff4j.store.AbstractFeatureStore
import org.ff4j.utils.json.FeatureJsonParser
import org.ff4j.web.api.resources.domain.FeatureApiBean
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.web.client.RestTemplate

/**
 * Implementation of store using Ribbon over {@link HttpClient} connection.
 *
 */
class FeatureStoreRibbon(
    val restTemplate: RestTemplate,
    val serviceId: String,
    val apiKey: String = ""
): AbstractFeatureStore() {

    private val log = LoggerFactory.getLogger(FeatureStoreRibbon::class.java)

//    val url: String ??

    /** header parameter to add if secured mode enabled. */
    val authorizationHeaderValue: String by lazy {
        if(apiKey.isNotEmpty()) buildAuthorization4ApiKey(apiKey)
        else ""
    }

    val storeRestUrl: String by lazy {
        "http://$serviceId/$FF4J_API_URL/$FF4J_RESOURCE_STORE/$FF4J_RESOURCE_FEATURES"
    }

    val groupsRestUrl: String by lazy {
        "http://$serviceId/$FF4J_API_URL/$FF4J_RESOURCE_STORE/$FF4J_RESOURCE_GROUPS"
    }

    private fun headers(): HttpHeaders {
        val headers = HttpHeaders()
        headers.setAccept(listOf(MediaType.APPLICATION_JSON))
        if(authorizationHeaderValue.isNotEmpty())
            headers.set(HEADER_AUTHORIZATION, authorizationHeaderValue)
        return headers
    }

    override fun read(featureUid: String?): Feature {

        if(featureUid == null || featureUid.length <= 0)
            throw IllegalArgumentException("[Assertion failed] - Parameter #0 (string)  must not be null nor empty")

        val response = restTemplate.exchange("$storeRestUrl/$featureUid",
            HttpMethod.GET, HttpEntity("parameters", headers()),
            String::class.java)

        if(response.statusCode.value() == 404)
            throw FeatureNotFoundException(featureUid)
        else if(!response.statusCode.is2xxSuccessful) {
            FeatureAccessException("Error when reaching API code:[${response.statusCodeValue}] MSG:${response.statusCode.reasonPhrase}")
        }
        return FeatureJsonParser.parseFeature(response.body)
    }

    override fun enable(uid: String?) {
        if(uid == null || uid.length <= 0)
            throw IllegalArgumentException("[Assertion failed] - Parameter #0 (string)  must not be null nor empty")

        val response =  restTemplate.postForEntity("$storeRestUrl/$uid/$OPERATION_ENABLE",
            HttpEntity("", headers()), String::class.java)

        if (response.statusCode.value() == 404) {
            throw FeatureNotFoundException(uid)
        }
    }

    override fun disable(uid: String?) {
        if(uid == null || uid.length <= 0)
            throw IllegalArgumentException("[Assertion failed] - Parameter #0 (string)  must not be null nor empty")

        val response =  restTemplate.postForEntity("$storeRestUrl/$uid/$OPERATION_DISABLE",
            HttpEntity("", headers()), String::class.java)

        if (response.statusCode.value() == 404) {
            throw FeatureNotFoundException(uid)
        }
    }

    override fun exist(uid: String?): Boolean {
        if(uid == null || uid.length <= 0)
            throw IllegalArgumentException("[Assertion failed] - Parameter #0 (string)  must not be null nor empty")

        val response =  restTemplate.exchange("$storeRestUrl/$uid",
            HttpMethod.GET, HttpEntity("", headers()), String::class.java)

        if(response.statusCode.is2xxSuccessful) return true
        if(response.statusCode.value() == 404) return false
        throw FeatureAccessException("Cannot check existence of feature, an HTTP error ${response.statusCodeValue} occured : ${response.statusCode.reasonPhrase}")
    }

    override fun create(fp: Feature?) {
        if(exist(fp?.uid ?: throw IllegalArgumentException("Feature cannot be null nor empty")))
            throw FeatureAlreadyExistException(fp.uid)

        val response = restTemplate.exchange("$storeRestUrl/${fp.uid}",
            HttpMethod.PUT, HttpEntity(FeatureApiBean(fp), headers()), String::class.java)

        if(!response.statusCode.is2xxSuccessful)
            throw FeatureAccessException("Cannot create feature, an HTTP error ${response.statusCodeValue}$OCCURED")
    }

    override fun readAll(): MutableMap<String, Feature> {
        val response =  restTemplate.exchange(storeRestUrl,
            HttpMethod.GET, HttpEntity("", headers()), String::class.java)
        if(!response.statusCode.is2xxSuccessful)
            throw FeatureAccessException("Cannot read features, an HTTP error ${response.statusCodeValue}$OCCURED")

        val features = FeatureJsonParser.parseFeatureArray(response.body)
        return features.associate { it.uid to it }.toMutableMap()
    }

    override fun delete(uid: String?) {
        if(uid == null || uid.length <= 0)
            throw IllegalArgumentException("[Assertion failed] - Parameter #0 (string)  must not be null nor empty")

        val response =  restTemplate.exchange("$storeRestUrl/$uid",
            HttpMethod.DELETE, HttpEntity("", headers()), String::class.java)
        if(response.statusCodeValue == 404) throw FeatureNotFoundException(uid)
        if(response.statusCodeValue != 204)
            throw FeatureAccessException("Cannot delete feature, an HTTP error ${response.statusCodeValue}$OCCURED")
    }

    override fun update(fp: Feature?) {
        if(!exist(fp?.uid ?: throw IllegalArgumentException("Feature cannot be null nor empty")))
            throw FeatureNotFoundException(fp.uid)

        val response = restTemplate.exchange("$storeRestUrl/${fp.uid}",
            HttpMethod.PUT, HttpEntity(FeatureApiBean(fp), headers()), String::class.java)

        if(response.statusCodeValue != 204)
            throw FeatureAccessException("Cannot update feature, an HTTP error ${response.statusCodeValue}$OCCURED")
    }

    override fun clear() {
        if(serviceId.isEmpty())
            throw IllegalArgumentException("[Assertion failed] - Parameter #0 (string)  must not be null nor empty")

        val response = restTemplate.exchange(
            "http://$serviceId/$FF4J_API_URL/$FF4J_RESOURCE_STORE/$STORE_CLEAR",
            HttpMethod.POST, HttpEntity("", headers()), String::class.java)

        if(!response.statusCode.is2xxSuccessful)
            throw FeatureAccessException("Cannot clear feature store - ${response.statusCode.reasonPhrase}")
    }

    override fun createSchema() {
        if(serviceId.isEmpty())
            throw IllegalArgumentException("[Assertion failed] - Parameter #0 (string)  must not be null nor empty")

        val response = restTemplate.exchange(
            "http://$serviceId/$FF4J_API_URL/$FF4J_RESOURCE_STORE/$STORE_CREATESCHEMA",
            HttpMethod.POST, HttpEntity("", headers()), String::class.java)

        if(!response.statusCode.is2xxSuccessful)
            throw FeatureAccessException("Cannot create feature store - ${response.statusCode.reasonPhrase}")
    }
}

fun buildAuthorization4ApiKey(apiKey: String): String {
    return PARAM_AUTHKEY + "=" + apiKey
}

const val OCCURED= " occured."
const val CANNOT_GRANT_ROLE_ON_FEATURE_AN_HTTP_ERROR = "Cannot grant role on feature, an HTTP error "

/** HTTP Parameter.  */
const val PARAM_AUTHKEY = "apiKey"

/** HTTP Header.  */
const val HEADER_AUTHORIZATION = "Authorization"

const val FF4J_API_URL = "api/ff4j"
const val FF4J_RESOURCE_STORE = "store"
const val FF4J_RESOURCE_FEATURES = "features"

const val FF4J_RESOURCE_GROUPS = "groups"

/** relative path for cache. */
const val STORE_CLEAR = "clear"

/** relative path for cache. */
const val STORE_CREATESCHEMA = "createSchema"

/** Custom operation on resource.  */
const val OPERATION_ENABLE = "enable"

/** Custom operation on resource.  */
const val OPERATION_DISABLE = "disable"

/** Custom operation on resource.  */
const val OPERATION_CHECK = "check"

/** Custom operation on resource.  */
const val OPERATION_GRANTROLE = "grantrole"

/** Custom operation on resource.  */
const val OPERATION_REMOVEROLE = "removerole"

/** Custom operation on resource.  */
const val OPERATION_ADDGROUP = "addGroup"