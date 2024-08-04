package com.ant.common.listeners

import com.ant.common.utils.Constants
import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs

abstract class AppBarStateChangeListener : AppBarLayout.OnOffsetChangedListener {

    enum class AppBarState {
        EXPANDED,
        COLLAPSED,
        IDLE
    }

    private var currentState = AppBarState.IDLE

    override fun onOffsetChanged(appBarLayout: AppBarLayout, vertifcalOffset: Int) {
        when {
            vertifcalOffset == 0 -> {
                if (currentState !== AppBarState.EXPANDED) {
                    onStateChanged(appBarLayout, AppBarState.EXPANDED)
                }
                currentState = AppBarState.EXPANDED
            }
            abs(vertifcalOffset) >= appBarLayout.totalScrollRange -> {
                val scrollPercentage: Float = (abs(vertifcalOffset) / appBarLayout.totalScrollRange).toFloat()

                if (scrollPercentage >= Constants.PERCENTAGE_TO_SHOW_TOOLBAR_COLLAPSED) {
                    if (currentState !== AppBarState.COLLAPSED) {
                        onStateChanged(appBarLayout, AppBarState.COLLAPSED)
                    }
                    currentState = AppBarState.COLLAPSED
                }
            }
            else -> {
                if (currentState !== AppBarState.IDLE) {
                    onStateChanged(appBarLayout, AppBarState.IDLE)
                }
                currentState = AppBarState.IDLE
            }
        }
    }

    abstract fun onStateChanged(appBarLayout: AppBarLayout, state: AppBarState)
}