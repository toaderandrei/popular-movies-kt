import com.ant.analytics.AnalyticsEvent
import com.ant.analytics.AnalyticsHelper


fun AnalyticsHelper.logAnalytics(
    type: AnalyticsEvent.Types,
    key: AnalyticsEvent.ParamKeys,
    value: String?,
) {
    this.logEvent(
        AnalyticsEvent(
            type = type, mutableListOf(
                AnalyticsEvent.Param(
                    key.toString(),
                    value
                )
            )
        )
    )
}