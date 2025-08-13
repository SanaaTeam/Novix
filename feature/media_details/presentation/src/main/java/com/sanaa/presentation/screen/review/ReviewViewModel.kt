package com.sanaa.presentation.screen.review

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.sanaa.presentation.details_base.BasePagingSource
import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.model.MediaTypeUiModel
import com.sanaa.presentation.model.ReviewUiModel
import com.sanaa.presentation.model.mapper.toReviewUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.Review
import exceptions.NoNetworkException
import exceptions.NovixAppException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import usecase.ManageMovieUseCase
import usecase.ManageTvShowUseCase
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val manageMovieDetails: ManageMovieUseCase,
    private val manageTvSeriesDetails: ManageTvShowUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
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
        updateState { copy(error = null, noInternetConnection = false, isLoading = true) }
        fetchReviews(mediaId)
    }

    private fun fetchReviews(id: Int) {
        tryToCollect(
            callee = {
                loadReviews(id, mediaType)
            },
            onCollect = ::onCollectReviews,
            onError = ::onFetchReviewsFailed
        )
    }

    private fun onCollectReviews(reviews: PagingData<ReviewUiModel>) {
        updateState { copy(reviews = flowOf(reviews), isLoading = false) }
    }

    private fun onFetchReviewsFailed(exception: NovixAppException) {
        if (exception is NoNetworkException) {
            updateState {
                copy(
                    noInternetConnection = true,
                    isLoading = false,
                    error = null
                )
            }
        } else {
            updateState { copy(isLoading = false, error = exception.message) }
        }
    }

    private fun loadReviews(id: Int, mediaType: MediaTypeUiModel): Flow<PagingData<ReviewUiModel>> {
        updateState { copy(isLoading = true) }
        return createPagingFlow(
            pagingSourceFactory = { createReviewsPagingDataSource(mediaType, id) },
            mapper = Review::toReviewUiModel
        )
    }

    private fun createReviewsPagingDataSource(
        mediaType: MediaTypeUiModel, id: Int,
    ): PagingSource<Int, Review> {
        return BasePagingSource { page ->
            if (mediaType == MediaTypeUiModel.MOVIE) {
                manageMovieDetails.getReviewsByMovieId(
                    id, page
                )
            } else {
                manageTvSeriesDetails.getTvShowReviews(
                    id, page
                )
            }
        }
    }
}