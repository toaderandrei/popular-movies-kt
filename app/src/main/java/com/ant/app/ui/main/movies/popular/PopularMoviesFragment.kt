package com.ant.app.ui.main.movies.popular

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ant.app.ui.main.base.movies.BaseMainListMoviesFragment
import com.ant.models.entities.MovieData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PopularMoviesFragment :
    BaseMainListMoviesFragment<PopularMoviesViewModel>() {

    override val viewModel: PopularMoviesViewModel by viewModels()

    override fun showDetailsScreen(movieData: MovieData) {
      //  findNavController().navigate(PopularMoviesFragmentDirections.toDetails(movieData.id))
    }

    override fun getThisFragment(): Fragment {
        return this@PopularMoviesFragment
    }
}