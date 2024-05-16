package com.ant.bindings

import android.view.View
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods

@BindingMethods(
    BindingMethod(type = View::class, attribute = "selected", method = "setSelected"),
    BindingMethod(type = View::class, attribute = "onLongClick", method = "setOnLongClickListener"),
)
class TmdbBindings