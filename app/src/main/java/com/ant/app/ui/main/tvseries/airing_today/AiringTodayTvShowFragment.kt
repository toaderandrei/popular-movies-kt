package com.ant.app.ui.main.tvseries.airing_today

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ant.app.ui.main.base.tvseries.BaseMainListTvSeriesFragment
import com.ant.models.entities.TvShow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AiringTodayTvShowFragment :
    BaseMainListTvSeriesFragment<AiringTodayTvShowViewModel>() {

    override val viewModel: AiringTodayTvShowViewModel by viewModels()

    override fun showDetailsScreen(tvShow: TvShow) {
       // findNavController().navigate(AiringTodayTvShowFragmentDirections.toDetails(tvShow.id))
    }

    override fun getThisFragment(): Fragment {
        return this@AiringTodayTvShowFragment
    }
}