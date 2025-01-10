package com.ant.app.ui.main.tvseries.ontv

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ant.app.ui.main.base.tvseries.BaseMainListTvSeriesFragment
import com.ant.models.entities.TvShow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnTvTvShowFragment :
    BaseMainListTvSeriesFragment<OnTvTvShowViewModel>() {

    override val viewModel: OnTvTvShowViewModel by viewModels()

    override fun showDetailsScreen(tvShow: TvShow) {
       // findNavController().navigate(OnTvTvShowFragmentDirections.toDetails(tvShow.id))
    }

    override fun getThisFragment(): Fragment {
        return this@OnTvTvShowFragment
    }
}