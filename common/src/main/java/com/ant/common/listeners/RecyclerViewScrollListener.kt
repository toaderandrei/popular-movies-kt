package com.ant.common.listeners

import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewScrollListener(private val mScrollCallback: OnScrollCallback) :
    RecyclerView.OnScrollListener() {

    var isLoading = MutableLiveData(false)
        set(value) {
            if (field != value) {
                field = value
            }
        }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)

        val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
        if (hasReachedTheEnd(linearLayoutManager) && isLoading.value == false) {
            mScrollCallback.onScrollUpdate()
        }
    }

    private fun hasReachedTheEnd(linearLayoutManager: LinearLayoutManager): Boolean {
        return linearLayoutManager.itemCount > 0 && linearLayoutManager.itemCount <= linearLayoutManager.findLastVisibleItemPosition() + 2
    }
}