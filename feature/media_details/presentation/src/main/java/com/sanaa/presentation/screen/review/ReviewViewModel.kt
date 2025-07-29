package com.sanaa.presentation.screen.review

import androidx.lifecycle.SavedStateHandle
import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.model.MediaTypeUiModel
import com.sanaa.presentation.model.toReviewUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import usecase.ManageMovieUseCase
import usecase.ManageTvSeriesUseCase
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val manageMovieDetails: ManageMovieUseCase,
    private val manageTvSeriesDetails: ManageTvSeriesUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<ReviewScreenUiState, ReviewScreenEffects>(
    initialState = ReviewScreenUiState(),
    defaultDispatcher = dispatcher
), ReviewScreenInteractionListener {

    private val mediaId: Int = checkNotNull(savedStateHandle["mediaId"])
    private val mediaType: MediaTypeUiModel = savedStateHandle
        .get<String>("mediaType")
        ?.let { MediaTypeUiModel.valueOf(it.uppercase()) }
        ?: error("mediaType argument missing")

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