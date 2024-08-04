package com.ant.analytics

interface AnalyticsHelper {
    fun logEvent(event: AnalyticsEvent)

    companion object {
        const val DEVICE_ID = "deviceId"
        const val APP_VERSION_NAME = "app_version_name"
        const val APP_VERSION_CODE = "app_version_code"
    }
}
