package com.sanaa.presentation.screen.review

import android.util.Log
import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.model.MediaTypeUiModel
import com.sanaa.presentation.model.toReviewUiModel
import details.usecase.movie.GetReviewsByMovieId
import details.usecase.tv_series.GetTvSeriesReviewsUseCase

class ReviewViewModel(
    private val mediaId: Int,
    private val mediaType: MediaTypeUiModel,
    private val getTvSeriesReviewsUseCase: GetTvSeriesReviewsUseCase,
    private val getMovieReviewsUseCase: GetReviewsByMovieId,

    ) : BaseViewModel<ReviewScreenUiState, ReviewScreenEffects>(ReviewScreenUiState()),
    ReviewScreenInteractionListener {
    init {
        Log.d("TAG", "MovieDetailsScreen: ${mediaType}${mediaId}")
        fetchTvSeriesReviews(mediaId, mediaType)
    }

    private fun fetchTvSeriesReviews(id: Int, mediaType: MediaTypeUiModel) {
        tryToExecute(callee = {
            updateState { it.copy(isLoading = true) }
            val reviews = if (mediaType == MediaTypeUiModel.MOVIE) {
                getMovieReviewsUseCase.execute(id)
            } else {
                getTvSeriesReviewsUseCase.execute(id)
            }
            updateState {
                it.copy(reviews = reviews.map {
                    it.toReviewUiModel()
                })
            }
        }, onSuccess = {
            updateState { it.copy(isLoading = false) }
        }, onError = {
            updateState { it.copy(isLoading = false, error = it.error) }
        })
    }

    override fun onBackClick() {
        emitEffect(ReviewScreenEffects.NavigateBack)
    }
}
