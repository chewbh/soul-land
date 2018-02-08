package io.boonlogic.soul_land.ff4j.toggle.strategy

import org.ff4j.core.FeatureStore
import org.ff4j.core.FlippingExecutionContext
import org.ff4j.strategy.AbstractFlipStrategy
import org.springframework.stereotype.Component

const val INIT_PARAM_EXPERIMENT_USERS = "experimentUsers"
const val PARAM_EXPERIMENT_USER = "user"

@Component
class ExperimentStrategy() : AbstractFlipStrategy() {

    private lateinit var userIds: List<String>

    override fun init(featureName: String?, initParam: MutableMap<String, String>?) {
        super.init(featureName, initParam)

        assertRequiredParameter(INIT_PARAM_EXPERIMENT_USERS)
        userIds = initParam?.get(INIT_PARAM_EXPERIMENT_USERS)
            ?.split(",")?.map { it.trim().toLowerCase() } ?: emptyList()
    }

    override fun evaluate(
        featureName: String?,
        store: FeatureStore?,
        executionContext: FlippingExecutionContext?
    ): Boolean {

        if(executionContext != null) {

            val user = executionContext.getString(PARAM_EXPERIMENT_USER)
            return userIds.contains(user.trim().toLowerCase())
        }
        return false
    }
}