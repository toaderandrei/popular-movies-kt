package com.ant.adapters

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import kotlinx.coroutines.CoroutineDispatcher

abstract class BasePagingAdapter<T : Any, V : ViewDataBinding>(
    dispatcher: CoroutineDispatcher, diffCallback: DiffUtil.ItemCallback<T>
) : PagingDataAdapter<T, CustomViewHolder<V>>(
    diffCallback = diffCallback,
    workerDispatcher = dispatcher,
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
        bind(holder, getItem(position)!!)
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