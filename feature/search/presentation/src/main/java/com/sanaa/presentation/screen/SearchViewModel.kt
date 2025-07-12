package com.sanaa.presentation.screen

import androidx.lifecycle.viewModelScope
import com.sanaa.presentation.base.BaseViewModel
import com.sanaa.presentation.state.ActorUiModel
import com.sanaa.presentation.state.MovieUiModel
import com.sanaa.presentation.state.SearchScreenUiState
import com.sanaa.presentation.state.TvShowUiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import usecase.AddRecentViewedUseCase
import usecase.ClearRecentViewedUseCase
import usecase.ClearSearchHistoryUseCase
import usecase.GetRecentViewedUseCase
import usecase.GetSearchHistoryUseCase
import usecase.SearchActorsUseCase
import usecase.SearchMoviesUseCase
import usecase.SearchTvSeriesUseCase
import usecase.search.MediaFilters

class SearchViewModel(
    private val searchMoviesUseCase: SearchMoviesUseCase,
    private val searchTvSeriesUseCase: SearchTvSeriesUseCase,
    private val searchActorsUseCase: SearchActorsUseCase,
    private val addRecentViewedUseCase: AddRecentViewedUseCase,
    private val getRecentViewedUseCase: GetRecentViewedUseCase,
    private val getSearchHistoryUseCase: GetSearchHistoryUseCase,
    private val clearRecentViewedUseCase: ClearRecentViewedUseCase,
    private val clearSearchHistoryUseCase: ClearSearchHistoryUseCase,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<SearchScreenUiState>(SearchScreenUiState(), dispatcher),
    SearchScreenInteractionsListener {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _filters = MutableStateFlow<MediaFilters?>(null)
    val filters = _filters.asStateFlow()

    init {
        loadResentSearchTitleList()
        loadResentViewedImageList()
        viewModelScope.launch {
            //  loadMediaByTab(query = "")
        }
        viewModelScope.launch {
            _searchQuery
                .debounce(500)
                .distinctUntilChanged()
                .collectLatest { query ->
                    if (query.isBlank()) {
                        updateState {
                            it.copy(
                                movies = emptyList(),
                                tvShows = emptyList(),
                                actors = emptyList(),
                                isLoading = false,
                                error = null
                            )
                        }
                    } else {
                        loadMediaByTab(query)
                    }
                }
        }
    }

    override fun onRecentSearchItemClicked() {

    }

    private fun loadResentViewedImageList() {
        updateState { it.copy(isLoading = true, error = null) }

        tryToExecute(
            callee = {
                getRecentViewedUseCase.execute().collectLatest { recentViewed ->
                    updateState {
                        it.copy(
                            resentViewedImageList = recentViewed.map { it.posterImageUrl }
                        )
                    }
                }
            },
            onError = { e ->
                updateState {
                    it.copy(isLoading = false, error = e.message ?: "Unknown error")
                }
            }
        )
    }


    private fun loadResentSearchTitleList() {
        updateState { it.copy(isLoading = true, error = null) }

        tryToExecute(
            callee = {
                getSearchHistoryUseCase.execute().collectLatest { searchHistory ->
                    updateState {
                        it.copy(
                            resentSearchTitleList = searchHistory.map { it.query }
                        )
                    }
                }
            },
            onError = { e ->
                updateState {
                    it.copy(isLoading = false, error = e.message ?: "Unknown error")
                }
            }
        )
    }


    private fun loadMediaByTab(query: String) {
        when (state.value.selectedTabIndex) {
            0 -> loadMovies(query)
            1 -> loadTvShows(query)
            2 -> loadActors(query)
        }
    }


    override fun onClearRecentViewClicked() {
        updateState { it.copy(isLoading = true, error = null) }

        tryToExecute(
            callee = {
                clearRecentViewedUseCase.execute()
            },
            onSuccess = {
                updateState { it.copy(isLoading = false) }
            },
            onError = { e ->
                updateState {
                    it.copy(isLoading = false, error = e.message ?: "Unknown error")
                }
            }
        )
    }


    override fun onClearRecentSearchClicked() {
        updateState { it.copy(isLoading = true, error = null) }

        tryToExecute(
            callee = {
                clearSearchHistoryUseCase.execute()
            },
            onSuccess = {
                updateState { it.copy(isLoading = false) }
            },
            onError = { e ->
                updateState {
                    it.copy(isLoading = false, error = e.message ?: "Unknown error")
                }
            }
        )
    }


    override fun onCancelRecentSearchItemClicked() {
        //     TODO("Not yet implemented")
    }


    override fun onTabSelected(index: Int) {
        updateState { it.copy(selectedTabIndex = index) }
        viewModelScope.launch {
            loadMediaByTab(_searchQuery.value)
        }
    }


    override fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        updateState { it.copy(searchQuery = query) }
    }

    override fun onFilterApplied(filters: MediaFilters?) {
        updateState { it.copy(filters = filters) }
        //     searchMediaByTab()
        loadMediaByTab(_searchQuery.value)
    }

    private fun loadMovies(query: String) {
        updateState { it.copy(isLoading = true, error = null) }

        tryToExecute(
            callee = { loadMoviesOperation(query) },
            onSuccess = ::onLoadMoviesSuccess,
            onError = ::onLoadMoviesError
        )
    }

    private suspend fun loadMoviesOperation(query: String): List<MovieUiModel> {
        return searchMoviesUseCase.execute(query, filters = state.value.filters).map {
            MovieUiModel(
                id = it.id,
                title = it.title,
                imageUrl = it.posterImageUrl,
                rating = ""
            )
        }
    }

    private fun onLoadMoviesSuccess(movies: List<MovieUiModel>) {
        updateState { it.copy(isLoading = false, movies = movies) }
    }

    private fun onLoadMoviesError(e: Exception) {
        updateState { it.copy(isLoading = false, error = e.message ?: "Unknown error") }
    }


    private fun loadTvShows(query: String) {
        updateState { it.copy(isLoading = true, error = null) }

        tryToExecute(
            callee = { loadTvShowsOperation(query) },
            onSuccess = ::onLoadTvShowsSuccess,
            onError = ::onLoadTvShowsError
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
        updateState { it.copy(isLoading = false, tvShows = tvShows) }
    }

    private fun onLoadTvShowsError(e: Exception) {
        updateState { it.copy(isLoading = false, error = e.message ?: "Unknown error") }
    }


    private fun loadActors(query: String) {
        updateState { it.copy(isLoading = true, error = null) }

        tryToExecute(
            callee = { loadActorsOperation(query) },
            onSuccess = ::onLoadActorsSuccess,
            onError = ::onLoadActorsError
        )
    }

    private suspend fun loadActorsOperation(query: String): List<ActorUiModel> {
        return searchActorsUseCase.execute(query).map {
            ActorUiModel(
                id = it.id,
                name = it.name,
                imageUrl = it.profileImageUrl
            )
        }
    }

    private fun onLoadActorsSuccess(actors: List<ActorUiModel>) {
        updateState { it.copy(isLoading = false, actors = actors) }
    }

    private fun onLoadActorsError(e: Exception) {
        updateState { it.copy(isLoading = false, error = e.message ?: "Unknown error") }
    }


    override fun onSaveIconClicked() {
    }


}
