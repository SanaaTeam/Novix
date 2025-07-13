package com.sanaa.presentation.screen

import androidx.lifecycle.viewModelScope
import com.sanaa.presentation.base.BaseViewModel
import com.sanaa.presentation.state.ActorUiModel
import com.sanaa.presentation.state.MovieUiModel
import com.sanaa.presentation.state.RecentSearchUiModel
import com.sanaa.presentation.state.RecentViewedUiModel
import com.sanaa.presentation.state.SearchScreenUiState
import com.sanaa.presentation.state.TvShowUiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import usecase.AddRecentViewedUseCase
import usecase.ClearRecentViewedUseCase
import usecase.ClearSearchHistoryUseCase
import usecase.GetRecentViewedUseCase
import usecase.GetSearchHistoryUseCase
import usecase.RemoveSearchHistoryUseCase
import usecase.SearchActorsUseCase
import usecase.SearchMoviesUseCase
import usecase.SearchTvSeriesUseCase
import usecase.search.MediaFilters
import usecase.search.MediaType
import usecase.search.RecentViewedMedia

class SearchViewModel(
    private val searchMoviesUseCase: SearchMoviesUseCase,
    private val searchTvSeriesUseCase: SearchTvSeriesUseCase,
    private val searchActorsUseCase: SearchActorsUseCase,
    private val addRecentViewedUseCase: AddRecentViewedUseCase,
    private val getRecentViewedUseCase: GetRecentViewedUseCase,
    private val getSearchHistoryUseCase: GetSearchHistoryUseCase,
    private val clearRecentViewedUseCase: ClearRecentViewedUseCase,
    private val clearSearchHistoryUseCase: ClearSearchHistoryUseCase,
    private val deleteSearchItem: RemoveSearchHistoryUseCase,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<SearchScreenUiState>(SearchScreenUiState()),
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

    private fun observeRecentViewedItems() {
        viewModelScope.launch {
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
                .catch { e -> onDataLoadError(e as Exception) }
                .collectLatest { viewed ->
                    updateState { it.copy(resentViewedMedia = viewed) }
                }
        }
    }

    private fun observeRecentSearchHistory() {
        viewModelScope.launch {
            getSearchHistoryUseCase.execute()
                .map { items -> items.map { RecentSearchUiModel(id = it.id, title = it.query) } }
                .catch { e -> onDataLoadError(e as Exception) }
                .collectLatest { queries ->
                    updateState { it.copy(resentSearchQueries = queries) }
                }
        }
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
            0 -> loadMovies(query)
            1 -> loadTvShows(query)
            2 -> loadActors(query)
        }
    }

    private fun loadMovies(query: String) {
        updateState { it.copy(isLoading = true, error = null) }
        tryToExecute({ loadMoviesOperation(query) }, ::onLoadMoviesSuccess, ::onDataLoadError)
    }

    private suspend fun loadMoviesOperation(query: String): List<MovieUiModel> {
        return searchMoviesUseCase.execute(query, filters = state.value.filters).map {
            MovieUiModel(id = it.id, title = it.title, imageUrl = it.posterImageUrl, rating = "")
        }
    }

    private fun onLoadMoviesSuccess(movies: List<MovieUiModel>) {
        updateState { it.copy(isLoading = false, movies = movies) }
    }

    private fun loadTvShows(query: String) {
        updateState { it.copy(isLoading = true, error = null) }
        tryToExecute({ loadTvShowsOperation(query) }, ::onLoadTvShowsSuccess, ::onDataLoadError)
    }

    private suspend fun loadTvShowsOperation(query: String): List<TvShowUiModel> {
        return searchTvSeriesUseCase.execute(query, filters = state.value.filters).map {
            TvShowUiModel(id = it.id, title = it.title, imageUrl = it.posterImageUrl, rating = "")
        }
    }

    private fun onLoadTvShowsSuccess(tvShows: List<TvShowUiModel>) {
        updateState { it.copy(isLoading = false, tvShows = tvShows) }
    }

    private fun loadActors(query: String) {
        updateState { it.copy(isLoading = true, error = null) }
        tryToExecute({ loadActorsOperation(query) }, ::onLoadActorsSuccess, ::onDataLoadError)
    }

    private suspend fun loadActorsOperation(query: String): List<ActorUiModel> {
        return searchActorsUseCase.execute(query).map {
            ActorUiModel(id = it.id, name = it.name, imageUrl = it.profileImageUrl)
        }
    }

    private fun onLoadActorsSuccess(actors: List<ActorUiModel>) {
        updateState { it.copy(isLoading = false, actors = actors) }
    }

    private fun onDataLoadError(e: Exception) {
        updateState { it.copy(isLoading = false, error = e.message ?: "Unknown error") }
    }


    override fun onSearchQueryChanged(query: String) {
        updateState { it.copy(searchQuery = query) }
    }

    override fun onTabSelected(index: Int) {
        updateState { it.copy(selectedTabIndex = index) }
        if (state.value.searchQuery.isNotBlank()) {
            loadMediaByTab(state.value.searchQuery)
        }
    }

    override fun onFilterApplied(filters: MediaFilters?) {
        updateState { it.copy(filters = filters) }

        val currentQuery = state.value.searchQuery
        if (currentQuery.isNotBlank()) {
            loadMediaByTab(currentQuery)
        }
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
            onSuccess = {},
            onError = {}
        )
    }

    override fun onClearRecentViewClicked() {
        tryToExecute(clearRecentViewedUseCase::execute, onSuccess = {}, onError = ::onDataLoadError)
    }

    override fun onClearRecentSearchClicked() {
        tryToExecute(
            clearSearchHistoryUseCase::execute,
            onSuccess = {},
            onError = ::onDataLoadError
        )
    }

    override fun onDeleteRecentSearchItem(id: Int) {
        updateState { it.copy(isLoading = true) }
        tryToExecute(callee = {
            deleteSearchItem.execute(id)
        }, onSuccess = {
            updateState { it.copy(isLoading = false) }
        }, onError = { e ->
            updateState {
                it.copy(isLoading = false, error = e.message ?: "Unknown error")
            }
        })
    }

    override fun onRecentSearchItemClicked(query: String) {
        updateState { it.copy(searchQuery = query) }

    }

    override fun onSaveIconClicked() { /* Not yet implemented */
    }
}