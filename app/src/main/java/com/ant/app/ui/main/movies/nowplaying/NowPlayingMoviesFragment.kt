package com.ant.app.ui.main.movies.nowplaying

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ant.app.ui.main.base.movies.BaseMainListMoviesFragment
import com.ant.models.entities.MovieData
import dagger.hilt.android.AndroidEntryPoint
import com.ant.app.R

@AndroidEntryPoint
class NowPlayingMoviesFragment :
    BaseMainListMoviesFragment<NowPlayingMoviesViewModel>() {

    override val viewModel: NowPlayingMoviesViewModel by viewModels()

    override fun showDetailsScreen(movieData: MovieData) {
        findNavController().navigate(NowPlayingMoviesFragmentDirections.toDetails(movieData.id))
    }

    override fun getThisFragment(): Fragment {
        return this@NowPlayingMoviesFragment
    }

    override fun getToolbarId(): Int {
        return R.id.toolbar
    }
}