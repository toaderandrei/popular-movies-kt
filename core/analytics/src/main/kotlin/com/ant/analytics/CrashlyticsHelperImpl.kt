package com.ant.analytics

import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CrashlyticsHelperImpl @Inject constructor(
    private val firebaseCrashlytics: FirebaseCrashlytics,
) : CrashlyticsHelper {
    override fun logError(throwable: Throwable) {
        firebaseCrashlytics.recordException(throwable)
    }
}