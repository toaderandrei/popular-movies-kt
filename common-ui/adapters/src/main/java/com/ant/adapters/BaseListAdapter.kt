package com.ant.adapters

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asExecutor

abstract class BaseListAdapter<T, V : ViewDataBinding>(
    dispatcher: CoroutineDispatcher, diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, CustomViewHolder<V>>(
    AsyncDifferConfig
        .Builder(diffCallback).setBackgroundThreadExecutor(dispatcher.asExecutor())
        .build()
) {

    override fun onCreateViewHolder(
        viewGroup: ViewGroup, layoutId: Int
    ): CustomViewHolder<V> {
        val binding = createViewDataBinding(viewGroup)
        binding.root.setOnClickListener {
            processClick(binding)
        }
        return CustomViewHolder(createViewDataBinding(viewGroup))
    }

    override fun onBindViewHolder(holder: CustomViewHolder<V>, position: Int) {
        bind(holder, getItem(position))
        holder.binding.executePendingBindings()
    }

    protected fun initClickListener(binding: V) {
        binding.root.setOnClickListener {
            processClick(binding)
        }
    }

    abstract fun processClick(binding: V)

    abstract fun bind(holder: CustomViewHolder<V>, item: T)

    abstract fun createViewDataBinding(parent: ViewGroup): V
}