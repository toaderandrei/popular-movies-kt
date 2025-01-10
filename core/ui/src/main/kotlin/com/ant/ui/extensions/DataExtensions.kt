package com.ant.ui.extensions

import kotlin.properties.ObservableProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

// todo verify and check this
inline fun <T> observable(
    initialValue: T,
    crossinline onChange: () -> Unit
): ReadWriteProperty<Any?, T> = object : ObservableProperty<T>(initialValue) {
    override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) = onChange()
}

//fun List<Genre>.toGenreAsStringList(): List<String> {
//    return this.mapNotNull {
//        it.name
//    }
//}
