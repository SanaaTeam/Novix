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

    init {
        searchMediaByTab()
    }

    fun onTabSelected(index: Int) {
        _uiState.update { it.copy(selectedTabIndex = index) }
        searchMediaByTab()
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        searchMediaByTab()
    }

    private fun searchMediaByTab() {
        when (_uiState.value.selectedTabIndex) {
            0 -> loadMovies()
            1 -> loadTvShows()
            2 -> loadActors()
        }
    }

    private fun loadMovies() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val movies = searchMoviesUseCase.execute(
                    query = _uiState.value.searchQuery,
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
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Unknown error") }
            }
        }
    }

    private fun loadTvShows() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val tvShows = searchTvSeriesUseCase.execute(
                    query = _uiState.value.searchQuery,
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
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Unknown error") }
            }
        }
    }

    private fun loadActors() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val actors = searchActorsUseCase.execute(query = _uiState.value.searchQuery).map {
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
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Unknown error") }
            }
        }
    }
}
