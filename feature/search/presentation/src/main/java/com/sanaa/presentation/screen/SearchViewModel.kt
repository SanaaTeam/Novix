package com.sanaa.presentation.screen

import androidx.lifecycle.viewModelScope
import com.sanaa.presentation.base.BaseViewModel
import com.sanaa.presentation.screen.state.ActorUiModel
import com.sanaa.presentation.screen.state.MovieUiModel
import com.sanaa.presentation.screen.state.RecentSearchUiModel
import com.sanaa.presentation.screen.state.RecentViewedUiModel
import com.sanaa.presentation.screen.state.SearchScreenUiState
import com.sanaa.presentation.screen.state.TvShowUiModel
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
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
        observeRecentViewedItems()
        observeRecentSearchHistory()
        observeSearchQueryChanges()
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQueryChanges() {
        viewModelScope.launch {
            state.map { it.searchQuery }
                .distinctUntilChanged()
                .debounce(500L)
                .collectLatest { query ->
                    if (query.isBlank()) {
                        clearSearchResults()
                    } else {
                        loadMediaByTab(query)
                    }
                }
        }
    }

    fun observeRecentViewedItems() {
        updateState {
            it.copy(
                isLoading = true, error = null, noInternetConnection = false
            )
        }

        tryToExecute(
            callee = {
                getRecentViewedUseCase.execute()
                    .map { items ->
                        items.map {
                            RecentViewedUiModel(
                                id = it.id,
                                imageUrl = it.posterImageUrl,
                                mediaType = it.mediaType.name,
                                isSaved = it.isSaved
                            )
                        }
                    }
                    .collectLatest { viewed ->
                        updateState {
                            it.copy(
                                recentViewedMedia = viewed, noInternetConnection = false
                            )
                        }
                    }
            },
            onError = ::onDataLoadError
        )
    }

    fun observeRecentSearchHistory() {
        tryToExecute(
            callee = {
                getSearchHistoryUseCase.execute()
                    .map { items ->
                        items.map {
                            RecentSearchUiModel(
                                id = it.id,
                                title = it.query
                            )
                        }
                    }
                    .collectLatest { queries ->
                        updateState {
                            it.copy(
                                recentSearchQueries = queries,
                                noInternetConnection = false
                            )
                        }
                    }
            },
            onError = ::onDataLoadError
        )
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
            ACTOR_INDEX -> loadActors(query)
        }
    }

    private fun loadMovies(query: String) {
        updateState { it.copy(isLoading = true, error = null, noInternetConnection = false) }
        tryToExecute(
            callee = { loadMoviesOperation(query) },
            onSuccess = ::onLoadMoviesSuccess,
            onError = ::onDataLoadError
        )
    }

    private suspend fun loadMoviesOperation(query: String): List<MovieUiModel> {
        return searchMoviesUseCase.execute(query, filters = state.value.filters).map {
            MovieUiModel(id = it.id, title = it.title, imageUrl = it.posterImageUrl, rating = "")
        }
    }

    private fun onLoadMoviesSuccess(movies: List<MovieUiModel>) {
        updateState { it.copy(isLoading = false, movies = movies, noInternetConnection = false) }
    }

    private fun loadTvShows(query: String) {
        updateState { it.copy(isLoading = true, error = null, noInternetConnection = false) }
        tryToExecute(
            callee = { loadTvShowsOperation(query) },
            onSuccess = ::onLoadTvShowsSuccess,
            onError = ::onDataLoadError
        )
    }

    private suspend fun loadTvShowsOperation(query: String): List<TvShowUiModel> {
        return searchTvSeriesUseCase.execute(query, filters = state.value.filters).map {
            TvShowUiModel(id = it.id, title = it.title, imageUrl = it.posterImageUrl, rating = "")
        }
    }

    private fun onLoadTvShowsSuccess(tvShows: List<TvShowUiModel>) {
        updateState { it.copy(isLoading = false, tvShows = tvShows, noInternetConnection = false) }
    }

    private fun loadActors(query: String) {
        updateState { it.copy(isLoading = true, error = null, noInternetConnection = false) }
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

    private fun onDataLoadError(e: Exception) {
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
            callee = {
                addRecentViewedUseCase.execute(
                    RecentViewedMedia(
                        id = viewed.id,
                        posterImageUrl = viewed.imageUrl,
                        mediaType = MediaType.valueOf(viewed.mediaType),
                        isSaved = viewed.isSaved
                    )
                )
            },
            onError = ::onDataLoadError
        )
    }

    override fun onClearRecentViewClicked() {
        tryToExecute(
            callee = clearRecentViewedUseCase::execute,
            onSuccess = {},
            onError = ::onDataLoadError
        )
    }

    override fun onClearRecentSearchClicked() {
        tryToExecute(
            clearSearchHistoryUseCase::execute,
            onSuccess = {},
            onError = ::onDataLoadError
        )
    }

    override fun onDeleteRecentSearchItem(id: Int) {
        updateState { it.copy(isLoading = true, error = null) }
        tryToExecute(
            callee = { deleteSearchItemUseCase.execute(id) },
            onSuccess = {
                updateState {
                    it.copy(
                        isLoading = false,
                        noInternetConnection = false
                    )
                }
            },
            ::onDataLoadError
        )
    }

    override fun onRecentSearchItemClicked(query: String) {
        updateState { it.copy(searchQuery = query) }
    }

    override fun onRetryClicked() {
        val currentQuery = state.value.searchQuery
        if (currentQuery.isNotBlank()) {
            loadMediaByTab(currentQuery)
        }
    }

    override fun onSaveIconClicked() {

    }

    companion object {
        const val MOVIE_INDEX = 0
        const val TV_SHOW_INDEX = 1
        const val ACTOR_INDEX = 2
    }
}