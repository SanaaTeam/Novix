package com.sanaa.presentation.screen.trendingTvShowScreen

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.sanaa.presentation.BaseViewModel
import com.sanaa.presentation.base.BasePagingSourceForHome
import com.sanaa.presentation.components.SnackData
import com.sanaa.presentation.state.GenreUiState
import com.sanaa.presentation.state.MediaItemUiState
import com.sanaa.presentation.state.mapper.toState
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.TvShow
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import service.VodStringProvider
import usecase.ManageTvShowUseCase
import javax.inject.Inject

@HiltViewModel
class TrendingTvShowsScreenViewModel @Inject constructor(
    private val manageTvShowUseCase: ManageTvShowUseCase,
    private val stringProvider: VodStringProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<TrendingTvShowsScreenUiState, TrendingTvShowsScreenEffect>(
    initialState = TrendingTvShowsScreenUiState(),
    defaultDispatcher = dispatcher
), TrendingTvShowsScreenInteractionListener {
    init {
        fetchGenres()
        loadTvShows()
    }

    private fun fetchGenres() {
        tryToExecute(
            onStart = { updateState { copy(isLoading = true) } },
            block = { manageTvShowUseCase.getTvShowGenres().map { it.toState() } },
            onSuccess = ::onLoadGenresSuccess,
            onError = ::onDataLoadError
        )
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
        emitEffect(TrendingTvShowsScreenEffect.NavigateToTvShowDetails(id))
    }

    override fun onBackClick() {
        emitEffect(TrendingTvShowsScreenEffect.NavigateBack)
    }

    override fun onRetryClick() {
        fetchGenres()
        loadTvShows()
    }

    override fun onSnackBarDismiss() {
        updateState { copy(snackBarData = null) }
    }

    private fun onDataLoadError(e: Throwable) {
        when (e) {
            is NoNetworkException -> {
                updateState {
                    copy(
                        isNoInternetConnection = true,
                        snackBarData =
                            SnackData(
                                message = stringProvider.noInternetConnectionError,
                                isError = true
                            )
                    )
                }
            }

            else -> {
                updateState {
                    copy(
                        isNoInternetConnection = false,
                        snackBarData =
                            SnackData(
                                message = stringProvider.somethingWentWrongError,
                                isError = true
                            )
                    )
                }
            }
        }
    }

    private fun createTvShowsPagingSource(onError: ((Throwable) -> Unit)? = ::onDataLoadError): PagingSource<Int, TvShow> {
        return BasePagingSourceForHome(onError = onError) { page ->
            manageTvShowUseCase.getTrendingTvShows(
                page = page,
                genreId = state.value.selectedGenreId
            )
        }
    }
}