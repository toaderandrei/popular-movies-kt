package com.ant.common.listeners

interface ShareCallback<T> {
    fun onShare(item: T)
}