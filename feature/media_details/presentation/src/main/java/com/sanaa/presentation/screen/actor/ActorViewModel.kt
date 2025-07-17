package com.sanaa.presentation.screen.actor

import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.module.toActorUiModel
import com.sanaa.presentation.module.toSeriesUiModel
import com.sanaa.presentation.module.toUiModel
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
        updateState { it.copy(isLoading = true) }
        tryToExecute(callee = {
            val series = getActorDetails.execute(actorId)
            val topMovies = getActorTopMovies.execute(actorId)
            val topSeries = getActorTopTvSeries.execute(actorId)
            val profileImages = getProfileImages.execute(actorId)
            val galleryImages = getGalleryImages.execute(actorId)
            updateState {
                it.copy(
                    actor = series.toActorUiModel(),
                    topMovies = topMovies.map { it.toUiModel() },
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
        emitEffect(
            ActorScreenEffects.NavigateToTopSeries(actorId)
        )
    }

    override fun onViewAllGalleryClicked() {
        emitEffect(
            ActorScreenEffects.NavigateToGallery(actorId)
        )
    }

    override fun onSeriesClicked(id: Int) {
        emitEffect(
            ActorScreenEffects.NavigateToSeriesDetails(id)
        )

    }

    override fun onMovieClicked(id: Int) {
        emitEffect(
            ActorScreenEffects.NavigateToMovieDetails(id)
        )
    }
}