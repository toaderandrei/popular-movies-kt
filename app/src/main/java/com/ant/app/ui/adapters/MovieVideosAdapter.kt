package com.ant.app.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.ant.adapters.BaseListAdapter
import com.ant.adapters.CustomViewHolder
import com.ant.models.entities.MovieVideo
import com.ant.layouts.databinding.ViewHolderMovieVideoItemBinding
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

open class MovieVideosAdapter constructor(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val callback: ((MovieVideo) -> Unit)?
) :
    BaseListAdapter<MovieVideo, ViewHolderMovieVideoItemBinding>(dispatcher,
        diffCallback = object : DiffUtil.ItemCallback<MovieVideo>() {
            override fun areItemsTheSame(oldItem: MovieVideo, newItem: MovieVideo): Boolean {
                return oldItem.id == newItem.id
                        && oldItem.key == newItem.key
                        && oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: MovieVideo, newItem: MovieVideo): Boolean {
                return oldItem.id == newItem.id &&
                        oldItem.key == newItem.key
                        && oldItem.movieId == newItem.movieId
            }
        }) {

    override fun createViewDataBinding(parent: ViewGroup): ViewHolderMovieVideoItemBinding {

        val binding: ViewHolderMovieVideoItemBinding =
            ViewHolderMovieVideoItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )

        initClickListener(binding)
        return binding
    }


    override fun processClick(binding: ViewHolderMovieVideoItemBinding) {
        binding.item?.let { it ->
            callback?.invoke(it)
        }
    }

    override fun bind(
        holder: CustomViewHolder<ViewHolderMovieVideoItemBinding>,
        item: MovieVideo
    ) {
        holder.binding.item = item
    }
}