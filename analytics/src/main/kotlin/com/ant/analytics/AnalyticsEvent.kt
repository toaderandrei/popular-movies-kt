package com.ant.analytics

data class AnalyticsEvent(
    val type: Types,
    val extras: List<Param> = emptyList(),
) {

    enum class Types {
        MAIN_SCREEN,
        ACCOUNT_PROFILE,
        LOGIN_SCREEN,
        LOGOUT_SCREEN,
        MOVIES_DETAILS_SCREEN,
        MOVIE_LIST_SCREEN,
        TVSHOWS_LIST_SCREEN,
        TVSHOWS_SCREEN,
    }

    data class Param(val key: String, val value: String?)
    enum class ParamKeys {
        MAIN_SCREEN,
        LOGIN_NAME,
        LOGOUT_USER,
        MOVIES_NAME,
        MOVIE_LIST_TYPE,
        TVSHOWS_NAME,
        TVSHOWS_LIST_TYPE,
    }
}
