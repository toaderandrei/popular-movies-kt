package com.ant.bindings

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import coil.load
import com.ant.common.listeners.FavoriteCallback
import com.ant.common.utils.YoutubeUtils
import com.ant.models.entities.ImageEntity
import com.ant.models.entities.MovieDetails
import com.ant.models.entities.TvShowDetails
import com.ant.models.model.GenreData
import com.ant.tmdb.old.PosterSizes
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.checkbox.MaterialCheckBox


@BindingAdapter("visibleGone")
fun showHide(view: View, show: Boolean) {
    view.visibility = if (show) View.VISIBLE else View.GONE
}

@BindingAdapter(value = ["image"], requireAll = false)
fun ImageView.loadImageUrl(imageEntity: ImageEntity?) {
    if (!imageEntity?.path.isNullOrEmpty()) {
        this.load(imageEntity) {
            target { drawable ->
                this@loadImageUrl.setImageDrawable(drawable)
            }
        }
    }
}

@BindingAdapter(value = ["image", "dimension", "defaultDrawable"], requireAll = false)
fun ImageView.loadImageUrl(
    urlPath: String?,
    posterSizes: String?,
    defaultDrawable: Drawable? = null
) {
    if (urlPath != null) {
        this.load(
            ImageEntity(
                urlPath, posterSizes ?: PosterSizes.W154.wSize
            )
        ) {
            target { drawable ->
                this@loadImageUrl.setImageDrawable(drawable)
            }
        }
    } else if (defaultDrawable != null) {
        this@loadImageUrl.setImageDrawable(defaultDrawable)
    }
}

@BindingAdapter(value = ["thumbnailVideo", "quality"], requireAll = false)
fun ImageView.loadThumbnail(key: String?, quality: YoutubeUtils.Quality? = null) {
    key?.let {
        this.load(
            YoutubeUtils.getQuality(it, quality ?: YoutubeUtils.Quality.HIGH)
        ) {
            target { drawable ->
                this@loadThumbnail.setImageDrawable(drawable)
            }
        }
    }
}

@BindingAdapter(value = ["toolbarTitleEnabled", "toolbarTitle"], requireAll = false)
fun setToolbarTitle(
    collapsingToolbarLayout: CollapsingToolbarLayout, isVisible: Boolean, title: String?
) {
    if (isVisible) {
        collapsingToolbarLayout.title = title
    } else {
        collapsingToolbarLayout.title = ""
    }
}


@BindingAdapter(value = ["text", "errorId"], requireAll = false)
fun bindingText(textView: AppCompatTextView, data: Int?, defaultResId: String? = null) {
    setTextViewText(textView, data?.toString(), defaultResId)
}


@BindingAdapter(value = ["text", "errorId"], requireAll = false)
fun bindingText(textView: AppCompatTextView, data: Double?, defaultResId: String? = null) {
    setTextViewText(textView, data?.toString(), defaultResId)
}

//todo fix defaultResId
@BindingAdapter(value = ["rating", "errorId"], requireAll = false)
fun bindingRatingText(textView: AppCompatTextView, data: String?, defaultResId: String? = null) {
    setTextViewText(textView, data, defaultResId)
}

@BindingAdapter(value = ["releaseDate", "errorId"], requireAll = false)
fun bindingReleaseDateText(
    textView: AppCompatTextView,
    data: String?,
    defaultResId: String? = null
) {
    setTextViewText(textView, data, defaultResId)
}

@BindingAdapter(value = ["text", "errorId"], requireAll = false)
fun bindingText(textView: AppCompatTextView, data: Float?, defaultResId: String? = null) {
    setTextViewText(textView, data?.toString(), defaultResId)
}

@BindingAdapter(value = ["genres"])
fun bindingText(textView: AppCompatTextView, data: List<GenreData>?) {
    val result = data?.joinToString { ", " }
    textView.text = result
}

private fun setTextViewText(
    textView: AppCompatTextView,
    data: String?,
    defaultResId: String?
) {
    textView.text = (data ?: defaultResId ?: "").toString()
}

@BindingAdapter(value = ["fav_callback", "fav_item"], requireAll = false)
fun setSaveMoviesFavoriteCallback(
    view: MaterialCheckBox,
    favoriteCallback: FavoriteCallback<MovieDetails>?,
    favItem: MovieDetails?
) {
    view.setOnCheckedChangeListener { _, isChecked ->
        if (isChecked) {
            favItem?.let {
                favoriteCallback?.onSave(it)
            }
        } else {
            favItem?.let {
                favoriteCallback?.delete(it)
            }
        }
    }
}

@BindingAdapter(value = ["fav_callback", "fav_item"], requireAll = false)
fun setSaveMoviesFavoriteCallback(
    view: MaterialCheckBox,
    favoriteCallback: FavoriteCallback<TvShowDetails>?,
    favItem: TvShowDetails?
) {
    view.setOnCheckedChangeListener { _, isChecked ->
        if (isChecked) {
            favItem?.let {
                favoriteCallback?.onSave(it)
            }
        } else {
            favItem?.let {
                favoriteCallback?.delete(it)
            }
        }
    }
}