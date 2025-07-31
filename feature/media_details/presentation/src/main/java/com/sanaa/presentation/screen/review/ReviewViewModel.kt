package com.sanaa.presentation.screen.review

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.sanaa.presentation.details_base.BasePagingSource
import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.model.MediaTypeUiModel
import com.sanaa.presentation.model.ReviewUiModel
import com.sanaa.presentation.model.toReviewUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.Review
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
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

    override fun onRetryClicked() {
        updateState { it.copy(error = null, noInternetConnection = false, isLoading = true) }
        fetchReviews(mediaId)
    }

    private fun fetchReviews(id: Int) {
        tryToCollect(
            callee = {
                loadReviews(id, mediaType)
            },
            onCollect = { reviews ->
                updateState {
                    it.copy(
                        reviews = flowOf(reviews), isLoading = false
                    )
                }
            },
            onError = { exception ->
                if (exception is NoNetworkException) {
                    updateState {
                        it.copy(
                            noInternetConnection = true,
                            isLoading = false,
                            error = null
                        )
                    }
                } else {
                    updateState { it.copy(isLoading = false, error = exception.message) }
                }
            }
        )
    }

    private fun loadReviews(id: Int, mediaType: MediaTypeUiModel): Flow<PagingData<ReviewUiModel>> {
        updateState { it.copy(isLoading = true) }
        return createPagingFlow(
            pagingSourceFactory = { createReviewsPagingDataSource(mediaType, id) },
            mapper = Review::toReviewUiModel
        )
    }

    private fun createReviewsPagingDataSource(
        mediaType: MediaTypeUiModel, id: Int
    ): PagingSource<Int, Review> {
        return BasePagingSource { page ->
            if (mediaType == MediaTypeUiModel.MOVIE) {
                manageMovieDetails.getReviewsByMovieId(
                    id, page
                )
            } else {
                manageTvSeriesDetails.getTvSeriesReviews(
                    id, page
                )
            }
        }
    }
}