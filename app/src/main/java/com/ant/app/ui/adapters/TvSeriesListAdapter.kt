package com.ant.app.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.ant.adapters.BaseListAdapter
import com.ant.adapters.CustomViewHolder
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
            return oldItem.id == newItem.id && oldItem.tvSeriesName == newItem.tvSeriesName && oldItem.movieOriginalName == newItem.movieOriginalName
        }

        override fun areContentsTheSame(oldItem: TvShow, newItem: TvShow): Boolean {
            return oldItem.tvSeriesName == newItem.tvSeriesName && oldItem.movieOriginalName == newItem.movieOriginalName
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