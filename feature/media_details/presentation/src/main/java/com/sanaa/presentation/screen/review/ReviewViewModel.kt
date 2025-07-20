package com.sanaa.presentation.screen.review

import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.model.MediaTypeUiModel
import com.sanaa.presentation.model.toReviewUiModel
import details.usecase.ManageMovieDetailsUseCase
import details.usecase.ManageTvSeriesDetailsUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class ReviewViewModel(
    private val mediaId: Int,
    private val mediaType: MediaTypeUiModel,
    private val manageMovieDetails: ManageMovieDetailsUseCase,
    private val manageTvSeriesDetails: ManageTvSeriesDetailsUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<ReviewScreenUiState, ReviewScreenEffects>(initialState = ReviewScreenUiState(),defaultDispatcher = dispatcher),
    ReviewScreenInteractionListener {

    init {
        fetchReviews(mediaId)
    }

    private fun fetchReviews(id: Int) {
        tryToExecute(
            callee = {
                updateState { it.copy(isLoading = true) }
                val reviews = if (mediaType == MediaTypeUiModel.MOVIE) {
                    manageMovieDetails.getReviewsByMovieId(id)
                } else {
                    manageTvSeriesDetails.getTvSeriesReviews(id)
                }
                updateState {
                    it.copy(reviews = reviews.map { it.toReviewUiModel() })
                }
            },
            onSuccess = {
                updateState { it.copy(isLoading = false) }
            },
            onError = {
                updateState { it.copy(isLoading = false, error = it.error) }
            }
        )
    }

    override fun onBackClick() {
        emitEffect(ReviewScreenEffects.NavigateBack)
    }
}