package com.ant.core.ui

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class CustomViewHolder<out V : ViewDataBinding> constructor(val binding: V) : RecyclerView.ViewHolder(binding.root)