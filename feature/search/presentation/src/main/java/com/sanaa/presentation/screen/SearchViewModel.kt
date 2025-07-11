package com.sanaa.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanaa.designsystem.R
import com.sanaa.presentation.state.ActorUiModel
import com.sanaa.presentation.state.MovieUiModel
import com.sanaa.presentation.state.SearchScreenUiState
import com.sanaa.presentation.state.TvShowUiModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel(), SearchScreenInteractionsListener {
    private val _uiState = MutableStateFlow(SearchScreenUiState())
    val uiState: StateFlow<SearchScreenUiState> = _uiState.asStateFlow()

    init {
        loadMovies()
    }

    override fun onTabSelected(index: Int) {
        _uiState.update { it.copy(selectedTabIndex = index) }

        when (index) {
            0 -> loadMovies()
            1 -> loadTvShows()
            2 -> loadActors()
        }
    }

    private fun loadMovies() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            delay(1000)
            val movies = listOf(
                MovieUiModel(1, "The Green Mile", R.drawable.movie_poster, "9.9"),
                MovieUiModel(2, "Prisoners", R.drawable.movie_poster, "9.0"),
                MovieUiModel(3, "Se7en", R.drawable.movie_poster, "8.6"),
                MovieUiModel(4, "Manifest", R.drawable.movie_poster, "8.2")
            )

            _uiState.update {
                it.copy(isLoading = false, movies = movies)
            }
        }
    }

    private fun loadTvShows() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            delay(1000)
            val tvShows = listOf(
                TvShowUiModel(1, "Breaking Bad", R.drawable.movie_poster, "9.8"),
                TvShowUiModel(2, "Dark", R.drawable.movie_poster, "9.0"),
                TvShowUiModel(3, "Stranger Things", R.drawable.movie_poster, "8.7")
            )

            _uiState.update {
                it.copy(isLoading = false, tvShows = tvShows)
            }
        }
    }

    private fun loadActors() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            delay(1000)

            val actors = listOf(
                ActorUiModel(1, "Tom Hanks", R.drawable.movie_poster),
                ActorUiModel(2, "Jake Gyllenhaal", R.drawable.movie_poster),
                ActorUiModel(3, "Morgan Freeman", R.drawable.movie_poster)
            )

            _uiState.update {
                it.copy(isLoading = false, actors = actors)
            }
        }
    }

    override fun onSaveIconClicked(){

    }
    override fun onCancelClicked(){

    }

    override fun onClearRecentSearchClicked(){

    }
    override fun onClearRecentViewClicked(){

    }


}
