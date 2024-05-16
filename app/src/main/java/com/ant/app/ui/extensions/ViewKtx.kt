package com.ant.app.ui.extensions

import android.view.View
import androidx.core.graphics.Insets
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import com.ant.app.databinding.ActivityMainBinding

fun setInsets(binding: ActivityMainBinding) {
    ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
        // Hide the bottom navigation view whenever the keyboard is visible.
        // This is needed for when doing a search.
        val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
        binding.bottomNavigationView.isVisible = !imeVisible

        // If we're showing the bottom navigation, add bottom and top padding.
        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        val bottomPadding = if (binding.bottomNavigationView.isVisible) {
            systemBars.bottom
        } else 0

        view.updatePadding(
            left = systemBars.left,
            right = systemBars.right,
            bottom = bottomPadding
        )
        // Consume the insets we've used.
        WindowInsetsCompat.Builder(insets).setInsets(
            WindowInsetsCompat.Type.systemBars(),
            Insets.of(0, systemBars.top, 0, systemBars.bottom - bottomPadding)
        ).build()
    }
}

fun View.doOnApplyWindowInsets(updateInsets: (View, WindowInsetsCompat, ViewPaddingState) -> Unit) {
    // Create a snapshot of the view's padding state in order to computer properly the padding below.
    val paddingState = snapshotStateForView(this)
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        updateInsets(v, insets, paddingState)
        insets
    }
    requestApplyInsetsWhenAttached()
}

/**
 * Call [View.requestApplyInsets] in a safe away. If we're attached it calls it straight-away.
 * If not it sets an [View.OnAttachStateChangeListener] and waits to be attached before calling
 * [View.requestApplyInsets].
 */
fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        requestApplyInsets()
    } else {
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.requestApplyInsets()
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}

private fun snapshotStateForView(view: View) = ViewPaddingState(
    view.paddingLeft,
    view.paddingTop,
    view.paddingRight,
    view.paddingBottom,
    view.paddingStart,
    view.paddingEnd
)

data class ViewPaddingState(
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int,
    val start: Int,
    val end: Int
)

