package com.sanaa.presentation.screen.genreTvShows

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.sanaa.presentation.details_base.BasePagingSource
import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.navigation.GenreTvShowsScreenRoute
import com.sanaa.presentation.model.TvShowUiState
import com.sanaa.presentation.model.mapper.toState
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.TvShow
import exceptions.NoNetworkException
import exceptions.NovixAppException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.ManageTvShowUseCase
import javax.inject.Inject

@HiltViewModel
class GenreTvShowsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val manageTvShowUseCase: ManageTvShowUseCase,
    private val checkIfUserIsLoggedInUseCase: CheckIfUserIsLoggedInUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<GenreTvShowsScreenUiState, GenreTvShowsEffects>(
    initialState = GenreTvShowsScreenUiState(),
    defaultDispatcher = dispatcher
), GenreTvShowsScreenInteractionListener {
    val route = GenreTvShowsScreenRoute(
        genreId = checkNotNull(savedStateHandle["genreId"]),
        genreName = checkNotNull(savedStateHandle["genreName"]),
    )

    init {
        updateUserLoggingStatus()
        getTvShowsByGenreId(route.genreId)
    }

    fun updateUserLoggingStatus() {
        tryToCollect(
            callee = { checkIfUserIsLoggedInUseCase.isLoggedIn() },
            onCollect = ::onCollectLoggedFlag,
        )
    }

    private fun onCollectLoggedFlag(isLogged: Boolean) {
        updateState { copy(userIsLoggedIn = isLogged) }
    }

    override fun onSaveIconClick() {
        if (!state.value.userIsLoggedIn) {
            updateState {
                copy(
                    showBottomSheet = true
                )
            }
        }
    }

    override fun onRetryClick() {
        updateState {
            copy(
                noInternetConnection = false,
                isLoading = true,
                error = null
            )
        }
        getTvShowsByGenreId(route.genreId)
    }

    override fun onBottomSheetDismiss() {
        updateState { copy(showBottomSheet = false) }
    }

    override fun onLoginButtonClick() {
        updateState { copy(showBottomSheet = false) }
        emitEffect(GenreTvShowsEffects.NavigateToLogin)
    }

    override fun onBackClick() {
        emitEffect(GenreTvShowsEffects.NavigateBack)
    }

    override fun onTvShowClick(id: Int) {
        emitEffect(GenreTvShowsEffects.NavigateToTvShowDetails(id))
    }

    private fun getTvShowsByGenreId(genreId: Int) {
        tryToCollect(
            callee = { loadTvShowsByGenreId(genreId) },
            onCollect = ::onCollectTvShowsByGenreId,
            onError = ::onGetShowsByGeneraIdFailed
        )
    }

    private fun onCollectTvShowsByGenreId(tvShows: PagingData<TvShowUiState>) {
        updateState {
            copy(
                title = route.genreName,
                tvShows = flowOf(tvShows),
                isLoading = false
            )
        }
    }

    private fun onGetShowsByGeneraIdFailed(exception: NovixAppException) {
        if (exception is NoNetworkException) {
            updateState { copy(noInternetConnection = true, isLoading = false, error = null) }
        } else {
            updateState { copy(error = exception.message, isLoading = false) }
        }
    }

    private fun loadTvShowsByGenreId(genreId: Int) = createPagingFlow(
        pagingSourceFactory = { createTvShowsPagingDataSource(genreId) },
        mapper = TvShow::toState
    )

    private fun createTvShowsPagingDataSource(
        genreId: Int,
    ): PagingSource<Int, TvShow> {
        return BasePagingSource { page ->
            manageTvShowUseCase.getTvShowsByGenre(
                genreId = genreId, page = page
            )
        }
    }
}