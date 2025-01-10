package com.ant.app.ui.main.tvseries.top

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ant.app.ui.main.base.tvseries.BaseMainListTvSeriesFragment
import com.ant.models.entities.TvShow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopRatedTvShowFragment :
    BaseMainListTvSeriesFragment<TopRatedTvShowViewModel>() {

    override val viewModel: TopRatedTvShowViewModel by viewModels()

    override fun showDetailsScreen(tvShow: TvShow) {
       // findNavController().navigate(TopRatedTvShowFragmentDirections.toDetails(tvShow.id))
    }

    override fun getThisFragment(): Fragment {
        return this@TopRatedTvShowFragment
    }
}