package com.sanaa.presentation.screen.review

import androidx.lifecycle.SavedStateHandle
import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.model.MediaTypeUiModel
import com.sanaa.presentation.model.toReviewUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import usecase.ManageMovieUseCase
import usecase.ManageTvSeriesUseCase

@HiltViewModel
class ReviewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val manageMovieDetails: ManageMovieUseCase,
    private val manageTvSeriesDetails: ManageTvSeriesUseCase
) : BaseViewModel<ReviewScreenUiState, ReviewScreenEffects>(
    initialState = ReviewScreenUiState(),
    defaultDispatcher = Dispatchers.IO
), ReviewScreenInteractionListener {

    private val mediaId: Int = checkNotNull(savedStateHandle["mediaId"])
    private val mediaType: MediaTypeUiModel = checkNotNull(savedStateHandle["mediaType"])


    init {
        fetchReviews(mediaId)
    }

    override fun onBackClick() {
        emitEffect(ReviewScreenEffects.NavigateBack)
    }

    private fun fetchReviews(id: Int) {
        tryToExecute(
            callee = { loadReviews(id) },
            onSuccess = {
                updateState { it.copy(isLoading = false) }
            },
            onError = { exception ->
                updateState { it.copy(isLoading = false, error = exception.message) }
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