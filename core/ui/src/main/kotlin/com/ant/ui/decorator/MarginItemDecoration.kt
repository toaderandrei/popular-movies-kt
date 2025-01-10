package com.ant.ui.decorator

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecoration(
    private val defaultMargin: Int,
    private val verticalMargin: Int = NOT_SET,
    private val firstItemMargin: Int = NOT_SET,
    private val lastItemMargin: Int = NOT_SET,
) : RecyclerView.ItemDecoration() {

    companion object {
        private const val NOT_SET = -1
    }

    private val spacingRect = Rect()

    init {
        with(spacingRect) {
            left = if (firstItemMargin == NOT_SET) {
                defaultMargin
            } else {
                firstItemMargin
            }
            top = if (verticalMargin == NOT_SET) {
                defaultMargin
            } else {
                verticalMargin
            }
            right = if (lastItemMargin == NOT_SET) {
                defaultMargin
            } else {
                lastItemMargin
            }
            bottom = if (verticalMargin == NOT_SET) {
                defaultMargin
            } else {
                verticalMargin
            }
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.set(spacingRect)
    }
}