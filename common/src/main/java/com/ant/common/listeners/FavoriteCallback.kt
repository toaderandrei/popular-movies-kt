package com.ant.common.listeners

interface FavoriteCallback<T> {
    fun onSave(item: T)
    fun delete(item: T)
}