package com.ant.ui.extensions

import android.view.View
import android.view.animation.AlphaAnimation


/**
 * todo - verify this
 * Allows easy listening to layout passing. Return [true] if you need the listener to keep being
 * attached.
 */
inline fun View.doOnSizeChange(crossinline action: (view: View) -> Boolean) {
    addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
        override fun onLayoutChange(
            view: View,
            left: Int,
            top: Int,
            right: Int,
            bottom: Int,
            oldLeft: Int,
            oldTop: Int,
            oldRight: Int,
            oldBottom: Int
        ) {
            if ((bottom - top) != (oldBottom - oldTop) || (right - left) != (oldRight - oldLeft)) {
                if (!action(view)) {
                    view.removeOnLayoutChangeListener(this)
                }
            }
        }
    })
}

fun View.startAnimation(duration: Long, visibility: Int) {
    val alphaAnimation = if (visibility == View.VISIBLE)
        AlphaAnimation(0f, 1f)
    else
        AlphaAnimation(1f, 0f)

    alphaAnimation.duration = duration
    alphaAnimation.fillAfter = true
    this.startAnimation(alphaAnimation)
}