package com.sanaa.presentation.screen.review

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.sanaa.presentation.details_base.BasePagingSource
import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.model.MediaTypeUiModel
import com.sanaa.presentation.model.ReviewUiModel
import com.sanaa.presentation.model.mapper.toReviewUiModel
import com.sanaa.presentation.navigation.ReviewsScreenRoute
import com.sanaa.presentation.screen.movieDetails.SnackData
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.Review
import exceptions.NoNetworkException
import exceptions.NovixAppException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import service.VodStringProvider
import usecase.ManageMovieUseCase
import usecase.ManageTvShowUseCase
import javax.inject.Inject

@HiltViewModel
class ReviewScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val manageMovieDetails: ManageMovieUseCase,
    private val manageTvShowDetails: ManageTvShowUseCase,
    private val stringProvider: VodStringProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<ReviewScreenUiState, ReviewScreenEffects>(
    initialState = ReviewScreenUiState(),
    defaultDispatcher = dispatcher
), ReviewScreenInteractionListener {
    val route = ReviewsScreenRoute(
        mediaId = checkNotNull(savedStateHandle["mediaId"]),
        mediaType = checkNotNull(savedStateHandle["mediaType"]),
    )

    init {
        fetchReviews(route.mediaId)
    }

    override fun onBackClick() {
        emitEffect(ReviewScreenEffects.NavigateBack)
    }

    override fun onRetryClicked() {
        updateState { copy(noInternetConnection = false, isLoading = true) }
        fetchReviews(route.mediaId)
    }

    private fun fetchReviews(id: Int) {
        tryToCollect(
            block = {
                loadReviews(id, route.mediaType)
            },
            onCollect = ::onCollectReviews,
            onError = ::onFetchReviewsFailed
        )
    }

    private fun onCollectReviews(reviews: PagingData<ReviewUiModel>) {
        updateState { copy(reviews = flowOf(reviews), isLoading = false) }
    }

    private fun onFetchReviewsFailed(exception: NovixAppException) {
        when (exception) {
            is NoNetworkException -> {
                updateState {
                    copy(
                        noInternetConnection = true,
                        isLoading = false,
                        snackBarData = SnackData(
                            message = stringProvider.noInternetConnectionError,
                            isError = true,
                        )
                    )
                }
            }

            else -> {
                updateState {
                    copy(
                        noInternetConnection = false,
                        isLoading = false,
                        snackBarData = SnackData(
                            message = stringProvider.somethingWentWrongError,
                            isError = true
                        )
                    )
                }
            }
        }
    }

    override fun onSnackBarDismiss() {
        updateState { copy(snackBarData = null) }
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
                manageTvShowDetails.getTvShowReviews(
                    id, page
                )
            }
        }
    }
}