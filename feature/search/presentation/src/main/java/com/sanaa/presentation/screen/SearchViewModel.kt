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
import usecase.SearchActorsUseCase
import usecase.SearchMoviesUseCase
import usecase.SearchTvSeriesUseCase

class SearchViewModel(
    private val searchMoviesUseCase: SearchMoviesUseCase,
    private val searchTvSeriesUseCase: SearchTvSeriesUseCase,
    private val searchActorsUseCase: SearchActorsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchScreenUiState())
    val uiState: StateFlow<SearchScreenUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()


    init {
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

    private suspend fun loadMediaByTab(query: String) {
        when (_uiState.value.selectedTabIndex) {
            0 -> loadMovies(query)
            1 -> loadTvShows(query)
            2 -> loadActors(query)
        }
    }

    fun onTabSelected(index: Int) {
        _uiState.update { it.copy(selectedTabIndex = index) }
    }

    fun onSearchQueryChanged(query: String) {
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

}
