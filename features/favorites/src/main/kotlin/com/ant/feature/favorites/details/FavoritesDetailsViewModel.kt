package com.ant.feature.favorites.details
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoritesDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,

) : ViewModel() {

}