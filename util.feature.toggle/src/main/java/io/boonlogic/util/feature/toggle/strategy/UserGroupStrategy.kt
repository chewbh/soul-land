package io.boonlogic.util.feature.toggle.strategy

import org.ff4j.core.FeatureStore
import org.ff4j.core.FlippingExecutionContext
import org.ff4j.strategy.AbstractFlipStrategy

/** Parameter to specify list of granted users (in username), delimited by ',' */
const val PARAM_GRANTED_GROUP_LIST = "groups"

/** Parameter to be checked in the context */
const val PARAM_GROUP_NAMES = "groupNames"

class UserGroupStrategy() : AbstractFlipStrategy() {

    private lateinit var rawGroups: String
    private lateinit var groups: Set<String>

    constructor(groups: String): this() {
        rawGroups = groups
        this.groups = loadRawGroups(rawGroups)
    }

    override fun init(featureName: String?, initParams: MutableMap<String, String>?) {
        super.init(featureName, initParams)

        rawGroups = initParams?.get(PARAM_GRANTED_GROUP_LIST) ?:
                throw IllegalArgumentException("invalid users list")
        groups = loadRawGroups(rawGroups)
    }

    override fun evaluate(
        featureName: String?,
        store: FeatureStore?,
        executionContext: FlippingExecutionContext?
    ): Boolean {
        if(executionContext != null && executionContext.containsKey(PARAM_GROUP_NAMES)) {
            if(executionContext.getString(PARAM_GROUP_NAMES).isNullOrEmpty()) return false
            else {
                val groupNames = executionContext.getString(PARAM_GROUP_NAMES)
                return groups.intersect(loadRawGroups(groupNames)).size > 0
            }
        }
        throw IllegalArgumentException(
            "To work with ${javaClass.name} you must provide $PARAM_GROUP_NAMES parameter in execution context")
    }

    private fun loadRawGroups(rawIds: String) =
        rawIds.split(',')
            .map { it.trim().toLowerCase() }
            .filter { it.isNotEmpty() }
            .toSet()
}