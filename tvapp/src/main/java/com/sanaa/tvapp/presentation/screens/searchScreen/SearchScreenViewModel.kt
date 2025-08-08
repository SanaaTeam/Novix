package com.sanaa.tvapp.presentation.screens.searchScreen

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import androidx.paging.map
import com.sanaa.tvapp.presentation.screens.searchScreen.mapper.toUiState
import com.sanaa.tvapp.base.TvBasePagingSource
import com.sanaa.tvapp.base.TvBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.Actor
import entity.Movie
import entity.TvSeries
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import usecase.search.SearchUseCase
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : TvBaseViewModel<SearchTvScreenUiState, Unit>(
    SearchTvScreenUiState(),
    dispatcher
), SearchScreenInteractionListener {

    init {
        observeSearchQueryChanges()
    }

    fun observeSearchQueryChanges() {
        tryToCollect(
            callee = ::observeSearchQueryFlow,
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
            it.copy(
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
        updateState { it.copy(selectedTabIndex = index) }
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
        setLoadingState()
        tryToCollect(
            callee = { loadTvShowsOperation(query) },
            onCollect = ::onTvShowsLoaded,
            onError = ::onDataLoadError
        )
    }

    private fun loadMovies(query: String) {
        setLoadingState()
        tryToCollect(
            callee = { loadMoviesOperation(query) },
            onCollect = ::onMoviesLoaded,
            onError = ::onDataLoadError
        )
    }

    private fun loadActors(query: String) {
        setLoadingState()
        tryToCollect(
            callee = { loadActorsOperation(query) },
            onCollect = ::onActorsLoaded,
            onError = ::onDataLoadError
        )
    }

    private fun setLoadingState() {
        updateState { it.copy(isLoading = true, error = null, noInternetConnection = false) }
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
            mapper = TvSeries::toUiState
        )
    }

    private fun loadMoviesOperation(query: String): Flow<PagingData<MovieUiModel>> {
        return createPagingFlow(
            pagingSourceFactory = { createMoviesPagingSource(query) },
            mapper = Movie::toUiState
        )
    }

    private fun onMoviesLoaded(pagingData: PagingData<MovieUiModel>) {
        updateState { it.copy(movies = flowOf(pagingData)) }
        setSuccessState()
    }

    private fun onTvShowsLoaded(pagingData: PagingData<TvShowUiModel>) {
        updateState { it.copy(tvShows = flowOf(pagingData)) }
        setSuccessState()
    }

    private fun onActorsLoaded(pagingData: PagingData<ActorUiModel>) {
        updateState { it.copy(actors = flowOf(pagingData)) }
        setSuccessState()
    }

    private fun setSuccessState() {
        updateState { it.copy(isLoading = false, noInternetConnection = false) }
    }

    private fun onDataLoadError(e: Throwable) {
        updateState { currentState ->
            if (e is NoNetworkException) {
                currentState.copy(
                    noInternetConnection = true,
                    isLoading = false,
                    error = null
                )
            } else {
                val errorMessage = e.message ?: "An unexpected error occurred."
                currentState.copy(
                    isLoading = false,
                    error = errorMessage,
                    noInternetConnection = false
                )
            }
        }
    }

    fun createActorsPagingSource(query: String): PagingSource<Int, Actor> {
        return TvBasePagingSource { page ->
            searchUseCase.searchActors(query = query, page = page)
        }
    }

    fun createTvShowsPagingSource(query: String): PagingSource<Int, TvSeries> {
        return TvBasePagingSource { page ->
            searchUseCase.searchTvShows(query = query, page = page)
        }
    }

    fun createMoviesPagingSource(query: String): PagingSource<Int, Movie> {
        return TvBasePagingSource { page ->
            searchUseCase.searchMovies(
                query = query,
                page = page,
            )
        }
    }

    fun <T : Any, R : Any> createPagingFlow(
        pagingSourceFactory: () -> PagingSource<Int, T>,
        mapper: (T) -> R
    ): Flow<PagingData<R>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
                prefetchDistance = 4
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow.map { pagingData ->
            pagingData.map(mapper)
        }.cachedIn(viewModelScope)
    }

    override fun onSearchQueryChanged(query: String) {
        updateState { it.copy(searchQuery = query) }
    }

    override fun retrySearch() {
        loadMediaByTab(state.value.searchQuery)
    }

    override fun onActorClicked(id: Int) {
        TODO("Not yet implemented")
    }

    override fun onLoginButtonClick() {
        TODO("Not yet implemented")
    }

    companion object {
        private const val PAGE_SIZE = 20
        const val MOVIE_INDEX = 0
        const val TV_SHOW_INDEX = 1
    }
}