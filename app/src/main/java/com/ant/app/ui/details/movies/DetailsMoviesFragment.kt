package com.ant.app.ui.details.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ant.resources.R as R2
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.ant.app.databinding.FragmentDetailsMoviesBinding
import com.ant.app.ui.adapters.MovieCastsAdapter
import com.ant.app.ui.adapters.MovieVideosAdapter
import com.ant.app.ui.base.BaseFragment
import com.ant.common.decorator.MarginItemDecoration
import com.ant.common.listeners.AppBarStateChangeListener
import com.ant.common.listeners.FavoriteCallback
import com.ant.common.listeners.RetryCallback
import com.ant.common.logger.TmdbLogger
import com.ant.models.entities.MovieDetails
import com.ant.common.utils.Constants.TMDB_KEY_ID
import com.ant.models.model.MoviesState
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.properties.Delegates

@AndroidEntryPoint
class DetailsMoviesFragment : BaseFragment<DetailsMovieViewModel, FragmentDetailsMoviesBinding>() {

    override val viewModel: DetailsMovieViewModel by viewModels()

    private var tmdbMovieId by Delegates.notNull<Long>()

    @Inject
    lateinit var logger: TmdbLogger

    private lateinit var movieCastAdapter: MovieCastsAdapter
    private lateinit var movieVideosAdapter: MovieVideosAdapter
    private val args: DetailsMoviesFragmentArgs by navArgs()

    override fun createViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentDetailsMoviesBinding {
        return FragmentDetailsMoviesBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tmdbMovieId = args.tmdbId

        if (tmdbMovieId == 0L) {
            throw IllegalArgumentException("tmdb movie id missing.")
        }
        updateCallbacks()

        movieCastAdapter = MovieCastsAdapter(callback = {
            logger.d("clicked on character: ${it.characterName}")
        })

        movieVideosAdapter = MovieVideosAdapter(callback = {
            logger.d("clicked on video: ${it.name}")
        })

        with(binding) {
            with(detailsMovieInfo.detailsMovieCastRv) {
                addItemDecoration(MarginItemDecoration(paddingLeft))
                adapter = movieCastAdapter
            }
            with(detailsMovieInfo.detailsMovieVideosRv) {
                addItemDecoration(MarginItemDecoration(paddingLeft))
                adapter = movieVideosAdapter
            }
        }

        with(viewModel) {
            loadMovieDetails(movieId = tmdbMovieId)
            lifecycleScope.launch {
                stateAsFlow.collect {
                    renderModels(it)
                }
            }
        }

        setToolbarTitle()
    }

    private fun setToolbarTitle() {
        val appCompatActivity = activity as AppCompatActivity
        appCompatActivity.setSupportActionBar(binding.detailsMoviesToolbar)
        appCompatActivity.supportActionBar?.setDisplayShowTitleEnabled(false);
        appCompatActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        with(binding) {
            detailsMovieAppbar.addOnOffsetChangedListener(appBarStateChangeListener())
        }
    }

    private fun renderModels(moviesState: MoviesState<MovieDetails>?) {
        moviesState?.let {
            logger.d("state: $moviesState")
            with(binding) {
                loadingState.isError = moviesState.error != null
                loadingState.errorMsg.error = moviesState.error?.message
                loadingState.isLoading = moviesState.loading
                moviesState.data?.let { movieDetailsData ->
                    item = movieDetailsData
                    movieCastAdapter.submitList(movieDetailsData.movieCasts)
                    movieDetailsData.movieCasts?.let {
                        movieCastAdapter.submitList(it)
                    }
                    movieDetailsData.videos?.let {
                        movieVideosAdapter.submitList(it)
                    }
                }
            }

        }
    }

    private fun updateCallbacks() {
        with(binding) {
            loadingState.callback = object : RetryCallback {
                override fun retry() {
                    logger.d("retry state.")
                    viewModel.retry(tmdbMovieId)
                }
            }

            detailsMovieInfo.favCallback = object : FavoriteCallback<MovieDetails> {
                override fun onSave(item: MovieDetails) {
                    logger.d("Save item to db: $item")
                    viewModel.saveAsFavorite(item)
                }

                override fun delete(item: MovieDetails) {
                    logger.d("delete item from db:$item")
                    viewModel.deleteFavorite(item)
                }
            }
        }
    }

    private fun appBarStateChangeListener(): AppBarStateChangeListener {
        return object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout, state: AppBarState) {
                if (state == AppBarState.COLLAPSED) {
                    onStateCollapsed()
                } else if (state == AppBarState.EXPANDED) {
                    onStateExpanded()
                }
            }
        }
    }

    private fun onStateExpanded() {
        binding.detailsViewMovieCollapsingToolbar.setStatusBarScrimColor(
            ContextCompat.getColor(
                activity?.applicationContext!!, android.R.color.transparent
            )
        )
        binding.titleVisible = false
        binding.detailsMovieAppbarContentId.detailsMovieImagePosterGroupDescription.visibility =
            View.VISIBLE
    }

    private fun onStateCollapsed() {
        binding.detailsViewMovieCollapsingToolbar.setStatusBarScrimColor(
            ContextCompat.getColor(
                activity?.applicationContext!!, R2.color.material_white
            )
        )
        binding.titleVisible = true
        binding.detailsMovieAppbarContentId.detailsMovieImagePosterGroupDescription.visibility =
            View.GONE
    }

    override fun getThisFragment(): Fragment {
        return this@DetailsMoviesFragment
    }

    companion object {
        fun create(id: Long): DetailsMoviesFragment {
            return DetailsMoviesFragment().apply {
                arguments = bundleOf(TMDB_KEY_ID to id)
            }
        }
    }
}