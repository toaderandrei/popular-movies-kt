package com.ant.app.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.ant.core.ui.BaseListAdapter
import com.ant.core.ui.CustomViewHolder
import com.ant.models.entities.MovieCast
import com.ant.layouts.databinding.ViewHolderMovieCastItemBinding
import com.ant.tmdb.old.PosterSizes
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

open class MovieCastsAdapter constructor(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val callback: ((MovieCast) -> Unit)?
) :
    BaseListAdapter<MovieCast, ViewHolderMovieCastItemBinding>(dispatcher,
        diffCallback = object : DiffUtil.ItemCallback<MovieCast>() {
            override fun areItemsTheSame(oldItem: MovieCast, newItem: MovieCast): Boolean {
                return oldItem.id == newItem.id
                        && oldItem.name == newItem.name
                        && oldItem.cast_id == newItem.cast_id
            }

            override fun areContentsTheSame(oldItem: MovieCast, newItem: MovieCast): Boolean {
                return oldItem.id == newItem.id &&
                        oldItem.name == newItem.name
                        && oldItem.movieId == newItem.movieId
            }
        }) {

    override fun createViewDataBinding(parent: ViewGroup): ViewHolderMovieCastItemBinding {

        val binding: ViewHolderMovieCastItemBinding =
            ViewHolderMovieCastItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )

        initClickListener(binding)
        return binding
    }

    override fun processClick(binding: ViewHolderMovieCastItemBinding) {
        binding.item?.let { it ->
            callback?.invoke(it)
        }
    }

    override fun bind(
        holder: CustomViewHolder<ViewHolderMovieCastItemBinding>,
        item: MovieCast
    ) {
        holder.binding.item = item
        holder.binding.posterSize = PosterSizes.W154.wSize
    }
}