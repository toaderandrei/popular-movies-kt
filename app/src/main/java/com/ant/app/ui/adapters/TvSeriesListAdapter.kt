package com.ant.app.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.ant.core.ui.BaseListAdapter
import com.ant.core.ui.CustomViewHolder
import com.ant.models.entities.TvShow
import com.ant.layouts.databinding.ViewHolderTvseriesItemDetailedBinding
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

open class TvSeriesListAdapter(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val callback: ((TvShow) -> Unit)?
) : BaseListAdapter<TvShow, ViewHolderTvseriesItemDetailedBinding>(
    dispatcher,
    diffCallback = object : DiffUtil.ItemCallback<TvShow>() {
        override fun areItemsTheSame(oldItem: TvShow, newItem: TvShow): Boolean {
            return oldItem.id == newItem.id && oldItem.name == newItem.name && oldItem.originalTitle == newItem.originalTitle
        }

        override fun areContentsTheSame(oldItem: TvShow, newItem: TvShow): Boolean {
            return oldItem.name == newItem.name && oldItem.originalTitle == newItem.originalTitle
        }
    }) {

    override fun createViewDataBinding(parent: ViewGroup): ViewHolderTvseriesItemDetailedBinding {

        val binding: ViewHolderTvseriesItemDetailedBinding =
            ViewHolderTvseriesItemDetailedBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )

        initClickListener(binding)
        return binding
    }

    @SuppressLint("NotifyDataSetChanged")
    fun loadResults(
        newList: List<TvShow>,
        pageSize: Int,
        isRefreshing: Boolean = false
    ) {
        val sizeBeforeUpdate = currentList.size
        submitList(newList) {
            attachedRecyclerView?.apply {
                if (isRefreshing) {
                    notifyDataSetChanged()
                    layoutManager?.scrollToPosition(0)
                } else {
                    // We know is not restoring data.
                    if (sizeBeforeUpdate > 0) {
                        val pageIndex = (newList.size - pageSize).coerceAtLeast(0)
                        layoutManager?.scrollToPosition(pageIndex)
                    }
                }
            }
        }
    }

    override fun processClick(binding: ViewHolderTvseriesItemDetailedBinding) {
        binding.item?.let { it ->
            callback?.invoke(it)
        }
    }

    override fun bind(
        holder: CustomViewHolder<ViewHolderTvseriesItemDetailedBinding>, item: TvShow
    ) {
        holder.binding.item = item
    }
}