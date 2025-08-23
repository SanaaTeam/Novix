package com.sanaa.tvapp.presentation.screens.searchScreen

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.sanaa.tvapp.base.BasePagingSource
import com.sanaa.tvapp.base.BaseViewModel
import com.sanaa.tvapp.presentation.screens.searchScreen.mapper.toUiState
import com.sanaa.tvapp.state.SnackData
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.Actor
import entity.Movie
import entity.TvShow
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import service.VodStringProvider
import usecase.search.SearchUseCase
import javax.inject.Inject


@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase,
    private val stringProvider: VodStringProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<SearchTvScreenUiState, SearchScreenEffect>(
    SearchTvScreenUiState(),
    dispatcher
), SearchScreenInteractionListener {

    init {
        observeSearchQueryChanges()
    }

    private fun observeSearchQueryChanges() {
        tryToCollect(
            block = ::observeSearchQueryFlow,
            onCollect = ::onSearchQueryChangedCollected,
            onError = ::onDataLoadError,
        )
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQueryFlow(): Flow<String> {
        return state.map { it.searchQuery }.distinctUntilChanged().debounce(500L)
    }

    private fun onSearchQueryChangedCollected(query: String) {
        if (query.isBlank()) clearSearchResults() else loadMediaByTab(query)
    }

    private fun clearSearchResults() {
        updateState {
            copy(
                movies = flowOf(PagingData.empty()),
                tvShows = flowOf(PagingData.empty()),
                actors = flowOf(PagingData.empty()),
                isLoading = false,
                error = null,
                noInternetConnection = false
            )
        }
    }

    override fun onTabSelected(index: Int) {
        if (index == state.value.selectedTabIndex) return
        updateState { copy(selectedTabIndex = index) }
        val searchQuery = state.value.searchQuery
        loadMediaByTab(searchQuery)
    }

    private fun loadMediaByTab(query: String) {
        if (query.isBlank()) return
        when (state.value.selectedTabIndex) {
            MOVIE_INDEX -> loadMovies(query.trim())
            TV_SHOW_INDEX -> loadTvShows(query.trim())
            else -> loadActors(query.trim())
        }
    }

    private fun loadTvShows(query: String) {
        tryToCollect(
            onStart = ::setLoadingState,
            block = { loadTvShowsOperation(query) },
            onCollect = ::onCollectTvShows,
            onError = ::onDataLoadError
        )
    }

    private fun onCollectTvShows(pagingData: PagingData<TvShowUiModel>) {
        updateState {
            copy(
                tvShows = flowOf(pagingData),
                isLoading = false,
                noInternetConnection = false
            )
        }
    }

    private fun loadMovies(query: String) {
        tryToCollect(
            onStart = ::setLoadingState,
            block = { loadMoviesOperation(query) },
            onCollect = ::onCollectMovies,
            onError = ::onDataLoadError
        )
    }

    private fun onCollectMovies(pagingData: PagingData<MovieUiModel>) {
        updateState {
            copy(
                movies = flowOf(pagingData),
                isLoading = false,
                noInternetConnection = false
            )
        }
    }

    private fun loadActors(query: String) {
        tryToCollect(
            onStart = ::setLoadingState,
            block = { loadActorsOperation(query) },
            onCollect = ::onCollectActors,
            onError = ::onDataLoadError
        )
    }

    private fun onCollectActors(pagingData: PagingData<ActorUiModel>) {
        updateState {
            copy(
                actors = flowOf(pagingData),
                isLoading = false,
                noInternetConnection = false
            )
        }
    }

    private fun setLoadingState() {
        updateState { copy(isLoading = true, error = null, noInternetConnection = false) }
    }

    private fun onDataLoadError(e: Throwable) {
        when (e) {
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
                        error = stringProvider.somethingWentWrongError,
                        snackBarData = SnackData(
                            message = stringProvider.somethingWentWrongError,
                            isError = true
                        )
                    )
                }
            }
        }
    }

    private fun loadActorsOperation(query: String): Flow<PagingData<ActorUiModel>> {
        return createPagingFlow(
            pagingSourceFactory = { createActorsPagingSource(query) },
            mapper = Actor::toUiState
        )
    }

    private fun loadTvShowsOperation(query: String): Flow<PagingData<TvShowUiModel>> {
        return createPagingFlow(
            pagingSourceFactory = { createTvShowsPagingSource(query) },
            mapper = TvShow::toUiState
        )
    }

    private fun loadMoviesOperation(query: String): Flow<PagingData<MovieUiModel>> {
        return createPagingFlow(
            pagingSourceFactory = { createMoviesPagingSource(query) },
            mapper = Movie::toUiState
        )
    }

    private fun createActorsPagingSource(query: String): PagingSource<Int, Actor> {
        return BasePagingSource { page ->
            searchUseCase.searchActors(
                query = query,
                page = page
            )
        }
    }

    private fun createTvShowsPagingSource(query: String): PagingSource<Int, TvShow> {
        return BasePagingSource { page ->
            searchUseCase.searchTvShows(
                query = query,
                page = page
            )
        }
    }

    private fun createMoviesPagingSource(query: String): PagingSource<Int, Movie> {
        return BasePagingSource { page ->
            searchUseCase.searchMovies(
                query = query,
                page = page,
            )
        }
    }

    override fun onSearchQueryChanged(query: String) {
        updateState { copy(searchQuery = query) }
    }

    override fun onRetryClicked() {
        loadMediaByTab(state.value.searchQuery)
    }

    override fun onActorClicked(id: Int) {
        emitEffect(SearchScreenEffect.NavigateToActorDetails(id))
    }

    override fun onMovieClicked(id: Int) {
        emitEffect(SearchScreenEffect.NavigateToMovieDetails(id))
    }

    override fun onTvShowClicked(id: Int) {
        emitEffect(SearchScreenEffect.NavigateToTvShowDetails(id))
    }

    override fun onSnackBarDismiss() {
        updateState { copy(snackBarData = null) }
    }

    companion object {
        const val MOVIE_INDEX = 0
        const val TV_SHOW_INDEX = 1
    }
}