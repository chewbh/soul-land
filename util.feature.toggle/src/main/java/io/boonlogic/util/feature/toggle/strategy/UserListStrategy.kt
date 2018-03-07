package io.boonlogic.util.feature.toggle.strategy

import org.ff4j.core.FeatureStore
import org.ff4j.core.FlippingExecutionContext
import org.ff4j.strategy.AbstractFlipStrategy

/** Parameter to specify list of granted users (in username), delimited by ',' */
const val PARAM_USER_LIST = "users"

/** Parameter to be checked in the context */
const val PARAM_USER_ID = "userId"

class UserListStrategy() : AbstractFlipStrategy() {

    private lateinit var rawUserIds: String
    private lateinit var userIds: Set<String>

    constructor(users: String): this() {
        rawUserIds = users
        userIds = loadRawUserId(rawUserIds)
    }

    override fun init(featureName: String?, initParams: MutableMap<String, String>?) {
        super.init(featureName, initParams)

        rawUserIds = initParams?.get(PARAM_USER_LIST)
                ?: throw IllegalArgumentException("invalid users list")
        userIds = loadRawUserId(rawUserIds)
    }

    override fun evaluate(
        featureName: String?,
        store: FeatureStore?,
        executionContext: FlippingExecutionContext?
    ): Boolean {
        if (executionContext != null && executionContext.containsKey(PARAM_USER_ID)) {
            return userIds.contains(executionContext.getString(PARAM_USER_ID))
        }
        throw IllegalArgumentException(
            "To work with ${javaClass.name} you must provide $PARAM_USER_ID parameter in execution context")
    }

    private fun loadRawUserId(rawIds: String) =
        rawIds.split(',')
            .map { it.trim().toLowerCase() }
            .filter { it.isNotEmpty() }
            .toSet()
}
