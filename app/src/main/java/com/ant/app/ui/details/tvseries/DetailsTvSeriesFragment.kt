package com.ant.app.ui.details.tvseries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.ant.app.databinding.FragmentDetailsTvshowBinding
import com.ant.app.ui.adapters.MovieCastsAdapter
import com.ant.app.ui.adapters.MovieVideosAdapter
import com.ant.app.ui.base.BaseFragment
import com.ant.common.decorator.MarginItemDecoration
import com.ant.common.listeners.AppBarStateChangeListener
import com.ant.common.listeners.FavoriteCallback
import com.ant.common.listeners.RetryCallback
import com.ant.common.logger.TmdbLogger
import com.ant.common.utils.Constants.TMDB_KEY_ID
import com.ant.models.entities.TvShowDetails
import com.ant.models.model.MoviesState
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.properties.Delegates
import com.ant.resources.R as R2

@AndroidEntryPoint
class DetailsTvSeriesFragment :
    BaseFragment<DetailsTvSeriesViewModel, FragmentDetailsTvshowBinding>() {

    override val viewModel: DetailsTvSeriesViewModel by viewModels()

    private var tmdbMovieId by Delegates.notNull<Long>()

    @Inject
    lateinit var logger: TmdbLogger

    private lateinit var tvSeriesCastAdapter: MovieCastsAdapter
    private lateinit var movieVideosAdapter: MovieVideosAdapter
    private val args: DetailsTvSeriesFragmentArgs by navArgs()

    override fun createViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentDetailsTvshowBinding {
        return FragmentDetailsTvshowBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tmdbMovieId = args.tmdbId

        if (tmdbMovieId == 0L) {
            throw IllegalArgumentException("tmdb movie id missing.")
        }
        updateCallbacks()

        tvSeriesCastAdapter = MovieCastsAdapter(callback = {
            logger.d("clicked on character: ${it.characterName}")
        })

        movieVideosAdapter = MovieVideosAdapter(callback = {
            logger.d("clicked on video: ${it.name}")
        })

        with(binding) {
            with(detailsTvseriesInfo.detailsTvseriesCastRv) {
                addItemDecoration(MarginItemDecoration(paddingLeft))
                adapter = tvSeriesCastAdapter
            }
            with(detailsTvseriesInfo.detailsTvseriesVideosRv) {
                addItemDecoration(MarginItemDecoration(paddingLeft))
                adapter = movieVideosAdapter
            }
        }

        with(viewModel) {
            stateAsLiveData.observe(viewLifecycleOwner) {
                renderModels(it)
            }
            loadMovieDetails(movieId = tmdbMovieId)
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

    private fun renderModels(tvSeriesState: MoviesState<TvShowDetails>?) {
        tvSeriesState?.let {
            logger.d("state: $tvSeriesState")
            with(binding) {
                loadingState.isError = tvSeriesState.error != null
                loadingState.errorMsg.error = tvSeriesState.error?.message
                loadingState.isLoading = tvSeriesState.loading
                tvSeriesState.data?.let { tvSeriesData ->
                    item = tvSeriesData
                    tvSeriesCastAdapter.submitList(tvSeriesData.tvSeriesCasts)
                    tvSeriesData.tvSeriesCasts?.let {
                        tvSeriesCastAdapter.submitList(it)
                    }
                    tvSeriesData.videos?.let {
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

            detailsTvseriesInfo.favCallback = object : FavoriteCallback<TvShowDetails> {
                override fun onSave(item: TvShowDetails) {
                    logger.d("Save item to db: $item")
                    viewModel.saveToDatabase(item)
                }

                override fun delete(item: TvShowDetails) {
                    logger.d("delete item from db:$item")
                    viewModel.deleteFromDatabase(item)
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
        binding.detailsTvseriesAppbarContentId.detailsMovieImagePosterGroupDescription.visibility =
            View.VISIBLE
    }

    private fun onStateCollapsed() {
        binding.detailsViewMovieCollapsingToolbar.setStatusBarScrimColor(
            ContextCompat.getColor(
                activity?.applicationContext!!, R2.color.material_white
            )
        )
        binding.titleVisible = true
        binding.detailsTvseriesAppbarContentId.detailsMovieImagePosterGroupDescription.visibility =
            View.GONE
    }

    override fun getThisFragment(): Fragment {
        return this@DetailsTvSeriesFragment
    }

    companion object {
        fun create(id: Long): DetailsTvSeriesFragment {
            return DetailsTvSeriesFragment().apply {
                arguments = bundleOf(TMDB_KEY_ID to id)
            }
        }
    }
}