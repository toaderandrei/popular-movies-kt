package com.ant.app.ui.main.movies.upcoming

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ant.app.ui.main.base.movies.BaseMainListMoviesFragment
import com.ant.models.entities.MovieData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpcomingMoviesFragment :
    BaseMainListMoviesFragment<UpcomingMoviesViewModel>() {

    override val viewModel: UpcomingMoviesViewModel by viewModels()

    override fun showDetailsScreen(movieData: MovieData) {
        // findNavController().navigate(UpcomingMoviesFragmentDirections.toDetails(movieData.id))
    }

    override fun getThisFragment(): Fragment {
        return this@UpcomingMoviesFragment
    }
}