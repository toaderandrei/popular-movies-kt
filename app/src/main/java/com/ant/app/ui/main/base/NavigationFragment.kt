package com.ant.app.ui.main.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.ant.app.R
import com.ant.app.ui.base.BaseFragment

interface ToolbarWithNavigation {
    fun registerToolbarWithNavigation(toolbar: Toolbar)
}

abstract class NavigationFragment<VIEW_MODEL : ViewModel, VIEW_BINDING : ViewDataBinding> :
    BaseFragment<VIEW_MODEL, VIEW_BINDING>() {

    private var toolbarWithNavigation: ToolbarWithNavigation? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ToolbarWithNavigation) {
            toolbarWithNavigation = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        toolbarWithNavigation = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val host = toolbarWithNavigation ?: return
        val mainToolbar: Toolbar = view.findViewById(R.id.toolbar) ?: return
        mainToolbar.apply {
            host.registerToolbarWithNavigation(this)
        }
    }
}