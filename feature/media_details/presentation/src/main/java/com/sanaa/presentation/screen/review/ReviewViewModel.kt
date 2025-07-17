package com.sanaa.presentation.screen.review

import android.util.Log
import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.module.toReviewUiModule
import details.usecase.tv_series.GetTvSeriesReviewsUseCase

class ReviewViewModel(
    private val seriesId: Int,
    private val getTvSeriesReviewsUseCase: GetTvSeriesReviewsUseCase,

    ) : BaseViewModel<ReviewScreenUiState, ReviewScreenEffects>(ReviewScreenUiState()),
    ReviewScreenInteractionListener {
    init {
        fetchTvSeriesReviews(seriesId)
    }

    private fun fetchTvSeriesReviews(id: Int) {
        tryToExecute(callee = {
            updateState { it.copy(isLoading = true) }
            val reviews = getTvSeriesReviewsUseCase.execute(id)
            updateState {
                Log.d("reviews", reviews.toString())
                it.copy(reviews = reviews.map {
                    it.toReviewUiModule()
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
