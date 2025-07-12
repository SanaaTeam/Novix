package com.sanaa.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanaa.presentation.state.ActorUiModel
import com.sanaa.presentation.state.MovieUiModel
import com.sanaa.presentation.state.SearchScreenUiState
import com.sanaa.presentation.state.TvShowUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import usecase.AddRecentViewedUseCase
import usecase.ClearRecentViewedUseCase
import usecase.ClearSearchHistoryUseCase
import usecase.GetRecentViewedUseCase
import usecase.GetSearchHistoryUseCase
import usecase.SearchActorsUseCase
import usecase.SearchMoviesUseCase
import usecase.SearchTvSeriesUseCase

class SearchViewModel(
    private val searchMoviesUseCase: SearchMoviesUseCase,
    private val searchTvSeriesUseCase: SearchTvSeriesUseCase,
    private val searchActorsUseCase: SearchActorsUseCase,
    private val addRecentViewedUseCase: AddRecentViewedUseCase,
    private val getRecentViewedUseCase: GetRecentViewedUseCase,
    private val getSearchHistoryUseCase: GetSearchHistoryUseCase,
    private val clearRecentViewedUseCase: ClearRecentViewedUseCase,
    private val clearSearchHistoryUseCase: ClearSearchHistoryUseCase,
) : ViewModel(), SearchScreenInteractionsListener {
    private val _uiState = MutableStateFlow(SearchScreenUiState())
    val uiState: StateFlow<SearchScreenUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()


    init {
        loadResentSearchTitleList()
        loadResentViewedImageList()
        viewModelScope.launch {
            loadMediaByTab(query = "")
        }
        viewModelScope.launch {
            _searchQuery
                .debounce(500)
                .distinctUntilChanged()
                .collectLatest { query ->
                    loadMediaByTab(query)
                }
        }
    }

    override fun onRecentSearchItemClicked() {

    }

    private fun loadResentViewedImageList() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                getRecentViewedUseCase.execute().collectLatest { recentViewed ->
                    _uiState.update {
                        it.copy(resentViewedImageList = recentViewed.map { it ->
                            it.posterImageUrl
                        })
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, error = e.message ?: "Unknown error")
                }
            }
        }
    }

    private fun loadResentSearchTitleList() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                getSearchHistoryUseCase.execute().collectLatest { searchHistory ->
                    _uiState.update {
                        it.copy(resentSearchTitleList = searchHistory.map { it ->
                            it.query
                        })
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, error = e.message ?: "Unknown error")
                }
            }
        }
    }


    private suspend fun loadMediaByTab(query: String) {
        when (_uiState.value.selectedTabIndex) {
            0 -> loadMovies(query)
            1 -> loadTvShows(query)
            2 -> loadActors(query)
        }
    }

    override fun onClearRecentViewClicked() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                clearRecentViewedUseCase.execute()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, error = e.message ?: "Unknown error")
                }
            }
        }
    }

    override fun onClearRecentSearchClicked() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                clearSearchHistoryUseCase.execute()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, error = e.message ?: "Unknown error")
                }
            }
        }
    }

    override fun onCancelRecentSearchItemClicked() {
        //     TODO("Not yet implemented")
    }


    override fun onTabSelected(index: Int) {
        _uiState.update { it.copy(selectedTabIndex = index) }
        viewModelScope.launch {
            loadMediaByTab(_searchQuery.value)
        }
    }


    override fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        _uiState.update { it.copy(searchQuery = query) }
    }


    private suspend fun loadMovies(query: String) {
        _uiState.update { it.copy(isLoading = true, error = null) }

        try {
            val movies = searchMoviesUseCase.execute(
                query = query,
                filters = null
            ).map {
                MovieUiModel(
                    id = it.id,
                    title = it.title,
                    imageUrl = it.posterImageUrl,
                    rating = ""
                )
            }

            _uiState.update {
                it.copy(isLoading = false, movies = movies)
            }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(isLoading = false, error = e.message ?: "Unknown error")
            }
        }
    }


    private suspend fun loadTvShows(query: String) {
        _uiState.update { it.copy(isLoading = true, error = null) }

        try {
            val tvShows = searchTvSeriesUseCase.execute(
                query = query,
                filters = null
            ).map {
                TvShowUiModel(
                    id = it.id,
                    title = it.title,
                    imageUrl = it.posterImageUrl,
                    rating = ""
                )
            }

            _uiState.update {
                it.copy(isLoading = false, tvShows = tvShows)
            }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(isLoading = false, error = e.message ?: "Unknown error")
            }
        }
    }


    private suspend fun loadActors(query: String) {
        _uiState.update { it.copy(isLoading = true, error = null) }

        try {
            val actors = searchActorsUseCase.execute(query).map {
                ActorUiModel(
                    id = it.id,
                    name = it.name,
                    imageUrl = it.profileImageUrl
                )
            }

            _uiState.update {
                it.copy(isLoading = false, actors = actors)
            }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(isLoading = false, error = e.message ?: "Unknown error")
            }
        }
    }


    override fun onSaveIconClicked() {
    }


}
