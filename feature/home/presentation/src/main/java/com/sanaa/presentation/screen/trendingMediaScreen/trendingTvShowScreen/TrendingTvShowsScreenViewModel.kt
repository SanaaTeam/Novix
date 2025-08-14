package com.sanaa.presentation.screen.trendingMediaScreen.trendingTvShowScreen

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.sanaa.presentation.BaseViewModel
import com.sanaa.presentation.base.BasePagingSourceForHome
import com.sanaa.presentation.screen.trendingMediaScreen.MediaListScreenInteractionListener
import com.sanaa.presentation.screen.trendingMediaScreen.TrendingMediaScreenEffect
import com.sanaa.presentation.screen.trendingMediaScreen.TrendingMediaScreenUiState
import com.sanaa.presentation.state.GenreUiState
import com.sanaa.presentation.state.MediaItemUiState
import com.sanaa.presentation.state.mapper.toState
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.TvShow
import exceptions.NoNetworkException
import exceptions.NovixAppException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import service.VodStringProvider
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.ManageTvShowUseCase
import javax.inject.Inject

@HiltViewModel
class TrendingTvShowsScreenViewModel @Inject constructor(
    private val manageTvShowUseCase: ManageTvShowUseCase,
    private val checkIfUserIsLoggedInUseCase: CheckIfUserIsLoggedInUseCase,
    private val stringProvider: VodStringProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<TrendingMediaScreenUiState, TrendingMediaScreenEffect>(
    initialState = TrendingMediaScreenUiState(),
    defaultDispatcher = dispatcher
), MediaListScreenInteractionListener {
    init {
        updateUserLoggingStatus()
        fetchGenres()
        loadTvShows()
    }

    fun updateUserLoggingStatus() {
        tryToCollect(
            block = { checkIfUserIsLoggedInUseCase.isLoggedIn() },
            onCollect = ::onCollectLoggedFlag,
        )
    }

    private fun onCollectLoggedFlag(isLogged: Boolean) {
        updateState { copy(userIsLoggedIn = isLogged) }
    }

    private fun fetchGenres() {
        tryToExecute(
            block = ::loadGenresOperation,
            onSuccess = ::onLoadGenresSuccess,
            onError = ::onDataLoadError
        )
    }

    private suspend fun loadGenresOperation(): List<GenreUiState> {
        updateState {
            copy(isLoading = true)
        }
        return manageTvShowUseCase.getTvShowGenres().map { it.toState() }
    }

    private fun onLoadGenresSuccess(genres: List<GenreUiState>) {
        updateState { copy(genreList = genres, isLoading = false, isNoInternetConnection = false) }
    }

    private fun loadTvShows() {
        tryToCollect(
            block = ::loadTvShowsOperation,
            onCollect = ::onLoadTvShowsSuccess,
            onError = ::onDataLoadError
        )
    }

    private fun loadTvShowsOperation(): Flow<PagingData<MediaItemUiState>> {
        return createPagingFlow(
            pagingSourceFactory = { createTvShowsPagingSource() },
            mapper = TvShow::toState
        )
    }

    private fun onLoadTvShowsSuccess(pagingData: PagingData<MediaItemUiState>) {
        updateState {
            copy(
                mediaList = flowOf(pagingData),
                isLoading = false,
                isNoInternetConnection = false
            )
        }
    }

    override fun onGenreClick(id: Int?) {
        if (id == state.value.selectedGenreId) return
        updateState {
            copy(selectedGenreId = id)
        }
        loadTvShows()
    }

    override fun onMediaClick(id: Int) {
        emitEffect(TrendingMediaScreenEffect.NavigateToMediaDetails(id))
    }

    override fun onSaveIconClick(media: MediaItemUiState) {
        if (!state.value.userIsLoggedIn) {
            updateState {
                copy(
                    showBottomSheet = true
                )
            }
        }
    }


    override fun onSaveToListSuccess() {
        emitEffect(TrendingMediaScreenEffect.ShowSuccess(message = stringProvider.addToListSuccess))
    }

    override fun onSaveToListFailure() {
        emitEffect(TrendingMediaScreenEffect.ShowError(message = stringProvider.addToListFailed))
    }

    override fun onBackClick() {
        emitEffect(TrendingMediaScreenEffect.NavigateBack)
    }

    override fun onRetryClick() {
        updateUserLoggingStatus()
        fetchGenres()
        loadTvShows()
    }

    override fun onLoginButtonClick() {
        emitEffect(TrendingMediaScreenEffect.NavigateToLogin)
    }

    override fun onDismissBottomSheet() {
        updateState { copy(showBottomSheet = false) }
    }


    private fun onDataLoadError(e: NovixAppException) {
        if (e is NoNetworkException) {
            updateState { copy(isNoInternetConnection = true) }
            emitEffect(TrendingMediaScreenEffect.ShowError(message = stringProvider.noInternetConnectionError))
        } else {
            updateState { copy(isNoInternetConnection = false) }
            emitEffect(TrendingMediaScreenEffect.ShowError(message = stringProvider.somethingWentWrongError))
        }
    }

    override fun onDismissSaveToListBottomSheet() {
        TODO("Not yet implemented")
    }

    override fun onCreateNewListClick() {
        TODO("Not yet implemented")
    }

    override fun onDismissAddListBottomSheet() {
        TODO("Not yet implemented")
    }

    private fun createTvShowsPagingSource(onError: ((NovixAppException) -> Unit)? = ::onDataLoadError): PagingSource<Int, TvShow> {
        return BasePagingSourceForHome(onError = onError) { page ->
            manageTvShowUseCase.getTrendingTvShows(
                page = page,
                genreId = state.value.selectedGenreId
            )
        }
    }
}