package com.sanaa.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import details.usecase.actor.GetActorDetailsUseCase
import details.usecase.actor.GetActorTopMoviesUseCase
import details.usecase.actor.GetActorTopTvSeriesUseCase
import details.usecase.actor.GetGalleryImagesUseCase
import details.usecase.actor.GetProfileImagesUseCase
import entity.Actor
import entity.Movie
import entity.TvSeries
import kotlinx.coroutines.launch

class ActorViewModel(
    private val getActorDetails: GetActorDetailsUseCase,
    private val getActorTopMovies: GetActorTopMoviesUseCase,
    private val getActorTopTvSeries: GetActorTopTvSeriesUseCase,
    private val getGalleryImages: GetGalleryImagesUseCase,
    private val getProfileImages: GetProfileImagesUseCase
) : ViewModel() {

    var actor by mutableStateOf<Actor?>(null)
        private set

    var topMovies by mutableStateOf<List<Movie>>(emptyList())
        private set

    var topTvSeries by mutableStateOf<List<TvSeries>>(emptyList())
        private set

    var galleryImages by mutableStateOf<List<String>>(emptyList())
        private set

    var profileImages by mutableStateOf<List<String>>(emptyList())
        private set

    var isLoading by mutableStateOf(true)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun loadActor(id: Int) {
        viewModelScope.launch {
            try {
                isLoading = true
                actor = getActorDetails.execute(id)
                topMovies = getActorTopMovies.execute(id)
                topTvSeries = getActorTopTvSeries.execute(id)
                galleryImages = getGalleryImages.execute(id)
                profileImages = getProfileImages.execute(id)
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }
}
