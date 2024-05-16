package com.ant.app.ui.main.tvseries.popular

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ant.app.ui.main.base.tvseries.BaseMainListTvSeriesFragment
import com.ant.models.entities.TvShow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PopularTvShowFragment :
    BaseMainListTvSeriesFragment<PopularTvShowViewModel>() {

    override val viewModel: PopularTvShowViewModel by viewModels()

    override fun showDetailsScreen(tvShow: TvShow) {
        findNavController().navigate(PopularTvShowFragmentDirections.toDetails(tvShow.id))
    }

    override fun getThisFragment(): Fragment {
        return this@PopularTvShowFragment
    }
}