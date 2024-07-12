package com.ant.app.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.ant.adapters.BaseListAdapter
import com.ant.adapters.CustomViewHolder
import com.ant.layouts.databinding.ViewHolderMovieItemDetailedBinding
import com.ant.models.entities.MovieData
import com.ant.tmdb.old.PosterSizes
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

open class MovieListAdapter(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val callback: ((MovieData) -> Unit)?,
) : BaseListAdapter<MovieData, ViewHolderMovieItemDetailedBinding>(dispatcher,
    diffCallback = object : DiffUtil.ItemCallback<MovieData>() {
        override fun areItemsTheSame(oldItem: MovieData, newItem: MovieData): Boolean {
            return oldItem.id == newItem.id
                    && oldItem.name == newItem.name
                    && oldItem.originalTitle == newItem.originalTitle
        }

        override fun areContentsTheSame(oldItem: MovieData, newItem: MovieData): Boolean {
            return oldItem.id == newItem.id &&
                    oldItem.name == newItem.name
                    && oldItem.originalTitle == newItem.originalTitle
                    && oldItem.genres == newItem.genres
        }
    }) {

    override fun createViewDataBinding(parent: ViewGroup): ViewHolderMovieItemDetailedBinding {

        val binding: ViewHolderMovieItemDetailedBinding =
            ViewHolderMovieItemDetailedBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )

        initClickListener(binding)
        return binding
    }

    @SuppressLint("NotifyDataSetChanged")
    fun loadResults(
        newList: List<MovieData>,
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

    override fun processClick(binding: ViewHolderMovieItemDetailedBinding) {
        binding.item?.let { it ->
            callback?.invoke(it)
        }
    }


    override fun bind(
        holder: CustomViewHolder<ViewHolderMovieItemDetailedBinding>,
        item: MovieData
    ) {
        holder.binding.item = item
        holder.binding.posterSize = PosterSizes.W154.wSize
    }
}