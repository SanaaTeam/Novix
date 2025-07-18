package com.sanaa.presentation.screen

import com.sanaa.presentation.base.BaseViewModel
import com.sanaa.presentation.screen.state.ActorUiModel
import com.sanaa.presentation.screen.state.MovieUiModel
import com.sanaa.presentation.screen.state.RecentSearchUiModel
import com.sanaa.presentation.screen.state.RecentViewedUiModel
import com.sanaa.presentation.screen.state.SearchScreenUiState
import com.sanaa.presentation.screen.state.SearchScreenUiState.Companion.ACTOR_INDEX
import com.sanaa.presentation.screen.state.SearchScreenUiState.Companion.MOVIE_INDEX
import com.sanaa.presentation.screen.state.SearchScreenUiState.Companion.TV_SHOW_INDEX
import com.sanaa.presentation.screen.state.TvShowUiModel
import com.sanaa.presentation.screen.state.mapper.toUiState
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import search.usecase.AddRecentViewedUseCase
import search.usecase.ClearRecentViewedUseCase
import search.usecase.ClearSearchHistoryUseCase
import search.usecase.GetRecentViewedUseCase
import search.usecase.GetSearchHistoryUseCase
import search.usecase.RemoveSearchHistoryUseCase
import search.usecase.SearchActorsUseCase
import search.usecase.SearchMoviesUseCase
import search.usecase.SearchTvSeriesUseCase
import search.usecase.search_param.MediaFilters
import search.usecase.search_param.MediaType
import search.usecase.search_param.RecentViewedMedia

class SearchViewModel(
    private val searchMoviesUseCase: SearchMoviesUseCase,
    private val searchTvSeriesUseCase: SearchTvSeriesUseCase,
    private val searchActorsUseCase: SearchActorsUseCase,
    private val addRecentViewedUseCase: AddRecentViewedUseCase,
    private val getRecentViewedUseCase: GetRecentViewedUseCase,
    private val getSearchHistoryUseCase: GetSearchHistoryUseCase,
    private val clearRecentViewedUseCase: ClearRecentViewedUseCase,
    private val clearSearchHistoryUseCase: ClearSearchHistoryUseCase,
    private val deleteSearchItemUseCase: RemoveSearchHistoryUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<SearchScreenUiState>(SearchScreenUiState(), dispatcher),
    SearchScreenInteractionsListener {

    init {
        observeSearchQueryChanges()
        observeRecentViewedItems()
        observeRecentSearchHistory()
    }

    @OptIn(FlowPreview::class)
    fun observeSearchQueryChanges() {
        tryToCollect(
            callee = { state.map { it.searchQuery }.distinctUntilChanged().debounce(500L) },
            onCollect = { if (it.isBlank()) clearSearchResults() else loadMediaByTab(it) },
            onError = ::onDataLoadError,
        )
    }

    fun observeRecentViewedItems() {
        updateState { it.copy(isLoading = true, error = null, noInternetConnection = false) }

        tryToCollect(
            callee = ::onGetRecentViewedItems,
            onCollect = ::onCollectRecentViewedItems,
            onError = ::onDataLoadError
        )
    }

    private suspend fun onGetRecentViewedItems(): Flow<List<RecentViewedUiModel>> {
        return getRecentViewedUseCase.execute().toUiState()
    }

    private fun onCollectRecentViewedItems(viewed: List<RecentViewedUiModel>) {
        updateState { it.copy(recentViewedMedia = viewed, noInternetConnection = false) }
    }

    fun observeRecentSearchHistory() {
        tryToCollect(
            callee = ::getRecentSearchHistory,
            onCollect = ::onCollectRecentSearchHistory,
            onError = ::onDataLoadError
        )
    }

    suspend fun getRecentSearchHistory(): Flow<List<RecentSearchUiModel>> {
        return getSearchHistoryUseCase.execute().toUiState()
    }

    fun onCollectRecentSearchHistory(queries: List<RecentSearchUiModel>) {
        updateState { it.copy(recentSearchQueries = queries, noInternetConnection = false) }
    }

    private fun clearSearchResults() {
        updateState {
            it.copy(
                movies = emptyList(),
                tvShows = emptyList(),
                actors = emptyList(),
                isLoading = false,
                error = null
            )
        }
    }

    private fun loadMediaByTab(query: String) {
        if (query.isBlank()) return
        when (state.value.selectedTabIndex) {
            MOVIE_INDEX -> loadMovies(query)
            TV_SHOW_INDEX -> loadTvShows(query)
            else -> loadActors(query)
        }
    }

    private fun loadMovies(query: String) {
        updateState {
            it.copy(
                isLoading = true,
                error = null,
                noInternetConnection = false
            )
        }
        tryToExecute(
            callee = { loadMoviesOperation(query) },
            onSuccess = ::onLoadMoviesSuccess,
            onError = ::onDataLoadError
        )
    }

    private suspend fun loadMoviesOperation(query: String): List<MovieUiModel> {
        return searchMoviesUseCase.execute(query, filters = state.value.filters).toUiState()
    }

    private fun onLoadMoviesSuccess(movies: List<MovieUiModel>) {
        updateState {
            it.copy(
                isLoading = false,
                movies = movies,
                noInternetConnection = false
            )
        }
    }

    private fun loadTvShows(query: String) {
        updateState {
            it.copy(
                isLoading = true,
                error = null,
                noInternetConnection = false
            )
        }
        tryToExecute(
            callee = { loadTvShowsOperation(query) },
            onSuccess = ::onLoadTvShowsSuccess,
            onError = ::onDataLoadError
        )
    }

    private suspend fun loadTvShowsOperation(query: String): List<TvShowUiModel> {
        return searchTvSeriesUseCase.execute(query, filters = state.value.filters).map {
            TvShowUiModel(
                id = it.id,
                title = it.title,
                imageUrl = it.posterImageUrl,
                rating = ""
            )
        }
    }

    private fun onLoadTvShowsSuccess(tvShows: List<TvShowUiModel>) {
        updateState {
            it.copy(
                isLoading = false,
                tvShows = tvShows,
                noInternetConnection = false
            )
        }
    }

    private fun loadActors(query: String) {
        updateState {
            it.copy(
                isLoading = true,
                error = null,
                noInternetConnection = false
            )
        }
        tryToExecute(
            callee = { loadActorsOperation(query) },
            onSuccess = ::onLoadActorsSuccess,
            onError = ::onDataLoadError
        )
    }

    private suspend fun loadActorsOperation(query: String): List<ActorUiModel> {
        return searchActorsUseCase.execute(query).map {
            ActorUiModel(id = it.id, name = it.name, imageUrl = it.profileImageUrl)
        }
    }

    private fun onLoadActorsSuccess(actors: List<ActorUiModel>) {
        updateState {
            it.copy(
                noInternetConnection = false,
                isLoading = false,
                actors = actors
            )
        }
    }

    private fun onDataLoadError(e: Throwable) {
        if (e is NoNetworkException)
            updateState {
                it.copy(
                    noInternetConnection = true,
                    isLoading = false,
                )
            }
        else
            updateState {
                it.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error",
                    noInternetConnection = false
                )
            }
    }


    override fun onSearchQueryChanged(query: String) {
        updateState { it.copy(searchQuery = query) }
    }

    override fun onTabSelected(index: Int) {
        if (index == state.value.selectedTabIndex) return
        if (index == ACTOR_INDEX) {
            updateState { it.copy(isFilterButtonVisible = false) }
        } else {
            updateState { it.copy(isFilterButtonVisible = true) }
        }
        updateState { it.copy(selectedTabIndex = index) }
        val searchQuery = state.value.searchQuery
        loadMediaByTab(searchQuery)
    }

    override fun onFilterApplied(filters: MediaFilters?) {
        updateState { it.copy(filters = filters) }

        val currentQuery = state.value.searchQuery
        loadMediaByTab(currentQuery)
    }

    override fun onSearchResultMediaClicked(viewed: RecentViewedUiModel) {
        tryToExecute(
            callee = { addRecentViewedMedia(viewed) },
            onError = ::onDataLoadError
        )
    }

    private suspend fun addRecentViewedMedia(viewed: RecentViewedUiModel) {
        addRecentViewedUseCase.execute(
            RecentViewedMedia(
                id = viewed.id,
                posterImageUrl = viewed.imageUrl,
                mediaType = MediaType.valueOf(viewed.mediaType),
                isSaved = viewed.isSaved
            )
        )
    }

    override fun onClearRecentViewClicked() {
        tryToExecute(
            callee = clearRecentViewedUseCase::execute,
            onError = ::onDataLoadError
        )
    }

    override fun onClearRecentSearchClicked() {
        tryToExecute(
            clearSearchHistoryUseCase::execute,
            onError = ::onDataLoadError
        )
    }

    override fun onDeleteRecentSearchItem(id: Int) {
        updateState { it.copy(isLoading = true, error = null) }
        tryToExecute(
            callee = { deleteSearchItemUseCase.execute(id) },
            onSuccess = {
                updateState { it.copy(isLoading = false, noInternetConnection = false) }
            },
            onError = ::onDataLoadError
        )
    }

    override fun onRecentSearchItemClicked(query: String) {
        updateState { it.copy(searchQuery = query) }
    }
}