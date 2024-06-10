package com.ant.models.source.delegates

import com.ant.models.session.SessionManager
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class SessionManagerDelegate(
    private val sessionManager: SessionManager
) : ReadOnlyProperty<Any?, SessionManager> {

    override fun getValue(thisRef: Any?, property: KProperty<*>): SessionManager {
        return sessionManager
    }
}