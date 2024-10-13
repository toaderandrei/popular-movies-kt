package com.ant.core.ui

import android.content.Context
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.ant.common.listeners.ToolbarWithNavigation
import com.ant.common.logger.TmdbLogger
import javax.inject.Inject

class ToolbarNavigationManager @Inject constructor(
    private val logger: TmdbLogger,
    private var toolbarWithNavigation: ToolbarWithNavigation? = null
) {

    fun attach(context: Context) {
        if (context is ToolbarWithNavigation) {
            toolbarWithNavigation = context
        }
    }

    fun detach() {
        toolbarWithNavigation = null
    }

    fun setupToolbar(view: View, id: Int) {
        val mainToolbar: Toolbar? = view.findViewById(id)
        mainToolbar?.let {
            toolbarWithNavigation?.registerToolbarWithNavigation(it)
            logger.d("register toolbar with navigation for: $view")
        }
    }
}
