package com.sanaa.presentation.screen.review

import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.model.MediaTypeUiModel
import com.sanaa.presentation.model.toReviewUiModel
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import usecase.ManageMovieUseCase
import usecase.ManageTvSeriesUseCase

class ReviewViewModel(
    private val mediaId: Int,
    private val mediaType: MediaTypeUiModel,
    private val manageMovieDetails: ManageMovieUseCase,
    private val manageTvSeriesDetails: ManageTvSeriesUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<ReviewScreenUiState, ReviewScreenEffects>(
    initialState = ReviewScreenUiState(),
    defaultDispatcher = dispatcher
),
    ReviewScreenInteractionListener {

    init {
        fetchReviews(mediaId)
    }


    override fun onBackClick() {
        emitEffect(ReviewScreenEffects.NavigateBack)
    }

    override fun onRetryClicked() {
        updateState { it.copy(error = null,noInternetConnection = false, isLoading = true) }
        fetchReviews(mediaId)
    }
    private fun fetchReviews(id: Int) {
        tryToExecute(
            callee = { loadReviews(id) },
            onSuccess = {
                updateState { it.copy(isLoading = false) }
            },
            onError = { exception ->
                if (exception is NoNetworkException) {
                    updateState { it.copy(noInternetConnection = true,isLoading = false, error = null) }
                }
                else {
                    updateState { it.copy(isLoading = false, error = exception.message) }
                }
            }
        )
    }

    private suspend fun loadReviews(id: Int) {

        updateState { it.copy(isLoading = true) }
        val reviews = if (mediaType == MediaTypeUiModel.MOVIE) {
            manageMovieDetails.getReviewsByMovieId(id)
        } else {
            manageTvSeriesDetails.getTvSeriesReviews(id)
        }
        updateState {
            it.copy(reviews = reviews.map { it.toReviewUiModel() })
        }
    }

}