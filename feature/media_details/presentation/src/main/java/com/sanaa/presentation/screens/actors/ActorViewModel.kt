package com.sanaa.presentation.screens.actors

import android.util.Log
import com.sanaa.presentation.details_base.BaseViewModel
import details.usecase.actor.GetActorDetailsUseCase
import details.usecase.actor.GetActorTopMoviesUseCase
import details.usecase.actor.GetActorTopTvSeriesUseCase
import details.usecase.actor.GetGalleryImagesUseCase
import details.usecase.actor.GetProfileImagesUseCase

class ActorViewModel(
    private val actorId: Int,
    private val getActorDetails: GetActorDetailsUseCase,
    private val getActorTopMovies: GetActorTopMoviesUseCase,
    private val getActorTopTvSeries: GetActorTopTvSeriesUseCase,
    private val getGalleryImages: GetGalleryImagesUseCase,
    private val getProfileImages: GetProfileImagesUseCase
) : BaseViewModel<ActorScreenUiState, ActorScreenEffects>(ActorScreenUiState()),
    ActorsScreenInteractionListener {

    init {
        getActorDetails(actorId)
    }

    private fun getActorDetails(actorId: Int) {
        Log.d("ActorVM", "fetching details for id=$actorId")
        updateState { it.copy(isLoading = true) }
        tryToExecute(callee = {
            Log.d("ActorVM", "fetching details for id=$actorId")
            val series = getActorDetails.execute(actorId)
            val topMovies = getActorTopMovies.execute(actorId)
            val topSeries = getActorTopTvSeries.execute(actorId)
            val profileImages = getProfileImages.execute(actorId)
            val galleryImages = getGalleryImages.execute(actorId)
            Log.d("ActorViewModel", "galleryImages: $galleryImages")
            Log.d("ActorViewModel", "profileImages: $profileImages")
            Log.d("ActorViewModel", "topSeries: $topSeries")
            Log.d("ActorViewModel", "topMovies: $topMovies")
            Log.d("ActorViewModel", "series: $series")
            updateState {
                it.copy(
                    actor = series.toActorUiModel(),
                    topMovies = topMovies.map { it.toMovieUiModel() },
                    topTvSeries = topSeries.map { it.toSeriesUiModel() },
                    profileImages = profileImages,
                    galleryImages = galleryImages
                )
            }
        }, onSuccess = {
            updateState {
                it.copy(isLoading = false)
            }
        }, onError = { e ->
            updateState {
                Log.e("ActorVM", "getActorDetails failed: ${e.toString()}")  // ② prints on error
                it.copy(error = it.error, isLoading = false)
            }
        })
    }

    override fun onBackClicked() {
        emitEffect(
            ActorScreenEffects.NavigateBack
        )
    }

    override fun onReadMoreClicked() {
        TODO("Not yet implemented")
    }

    override fun onTopMoviesClicked() {
        emitEffect(
            ActorScreenEffects.NavigateToTopMovies(actorId)
        )
    }

    override fun onTopSeriesClicked() {
        TODO("Not yet implemented")
    }

    override fun onViewAllGalleryClicked() {
        TODO("Not yet implemented")
    }
}