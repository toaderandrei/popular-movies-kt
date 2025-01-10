package com.ant.analytics

import android.os.Bundle
import com.ant.common.qualifiers.IoDispatcher
import com.ant.models.session.SessionManager
import com.google.firebase.BuildConfig
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsHelperImpl @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : AnalyticsHelper {

    override fun logEvent(event: AnalyticsEvent) {
        CoroutineScope(ioDispatcher).launch {
            val params = event.extras
            val newParams = params.toMutableList()
            setGlobalProperties(newParams)
            newParams.add(
                AnalyticsEvent.Param(
                    SessionManager.USERNAME, "test"//sessionManager.getUsername()
                )
            )
            newParams.add(
                AnalyticsEvent.Param(
                    SessionManager.SESSION_ID, "TestId"// todo: sessionManager.getSessionId()
                )
            )
            val bundle = Bundle()
            newParams.forEach {
                bundle.putString(it.key, it.value)
            }
            firebaseAnalytics.logEvent(event.type.name, bundle)
        }
    }

    private fun setGlobalProperties(
        params: MutableList<AnalyticsEvent.Param>
    ) {
        params.add(AnalyticsEvent.Param(AnalyticsHelper.APP_VERSION_NAME, BuildConfig.VERSION_NAME))
    }
}