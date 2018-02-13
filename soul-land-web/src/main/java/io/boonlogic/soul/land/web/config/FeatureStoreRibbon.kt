package io.boonlogic.soul.land.web.config

import org.ff4j.core.Feature
import org.ff4j.exception.FeatureAccessException
import org.ff4j.exception.FeatureNotFoundException
import org.ff4j.store.AbstractFeatureStore
import org.ff4j.utils.json.FeatureJsonParser
import org.slf4j.LoggerFactory
import org.springframework.web.client.RestTemplate


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
    val authorizationHeaderValue: String? by lazy {
        if(apiKey.isNotEmpty()) buildAuthorization4ApiKey(apiKey)
        else null
    }

    val storeRestUrl: String by lazy {
        "http://$serviceId/$FF4J_API_URL/$FF4J_RESOURCE_STORE/$FF4J_RESOURCE_FEATURES"
    }

    val groupsRestUrl: String by lazy {
        "http://$serviceId/$FF4J_API_URL/$FF4J_RESOURCE_STORE/$FF4J_RESOURCE_GROUPS"
    }

    override fun read(featureUid: String?): Feature {

        if(featureUid == null || featureUid.length <= 0)
            throw IllegalArgumentException("[Assertion failed] - Parameter #0 (string)  must not be null nor empty")

        val response = restTemplate.getForEntity("$storeRestUrl/$featureUid", String::class.java)
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
//        OPERATION_ENABLE
        restTemplate.postForEntity("$storeRestUrl/$uid")
    }

    override fun clear() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun create(fp: Feature?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun exist(featId: String?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(fp: Feature?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }



    override fun delete(fpId: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun readAll(): MutableMap<String, Feature> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

fun buildAuthorization4ApiKey(apiKey: String): String {
    return PARAM_AUTHKEY + "=" + apiKey
}