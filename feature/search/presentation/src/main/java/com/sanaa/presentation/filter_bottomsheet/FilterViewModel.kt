package com.sanaa.presentation.filter_bottomsheet

import com.sanaa.presentation.base.BaseViewModel
import com.sanaa.presentation.filter_bottomsheet.state.FilterUiState
import com.sanaa.presentation.filter_bottomsheet.state.GenreUiState
import com.sanaa.presentation.filter_bottomsheet.state.MediaTabFilters
import com.sanaa.presentation.screen.state.mapper.toDomain
import com.sanaa.presentation.screen.state.mapper.toState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import usecase.ManageMovieUseCase
import usecase.ManageTvSeriesUseCase
import usecase.search.search_param.MediaFilters
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val manageMovieUseCase: ManageMovieUseCase,
    private val manageTvSeriesUseCase: ManageTvSeriesUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<FilterUiState, Unit>(
    initialState = FilterUiState(),
    defaultDispatcher = dispatcher
), FilterBottomSheetInteractionsListener {
    var currentTabIndex = MOVIE_INDEX

    private val _filterResult = MutableSharedFlow<Pair<Int, MediaFilters?>>()
    val filterResult = _filterResult.asSharedFlow()

    init {
        fetchMovieGenres()
        fetchTvShowGenres()
    }

    override fun onYearRangeChanged(tabIndex: Int, newRange: ClosedFloatingPointRange<Float>) {
        updateState { currentState ->
            val currentFilters =
                if (tabIndex == MOVIE_INDEX) currentState.movieFilters else currentState.tvFilters
            val updatedFilters = currentFilters.copy(yearRange = newRange)

            currentState.copy(
                movieFilters = if (tabIndex == MOVIE_INDEX) updatedFilters else currentState.movieFilters,
                tvFilters = if (tabIndex == TV_SHOW_INDEX) updatedFilters else currentState.tvFilters
            )
        }
    }

    override fun onGenreSelected(tabIndex: Int, genre: GenreUiState) {
        updateState { currentState ->
            val currentFilters =
                if (tabIndex == MOVIE_INDEX) currentState.movieFilters else currentState.tvFilters

            val newSelectedGenres = currentFilters.selectedGenres.toMutableSet().apply {
                if (contains(genre)) remove(genre) else add(genre)
            }

            val newFilters = currentFilters.copy(selectedGenres = newSelectedGenres)

            currentState.copy(
                movieFilters = if (tabIndex == MOVIE_INDEX) newFilters else currentState.movieFilters,
                tvFilters = if (tabIndex == TV_SHOW_INDEX) newFilters else currentState.tvFilters
            )
        }
    }

    override fun onRatingChanged(tabIndex: Int, newRating: Int) {
        updateState { currentState ->
            val currentFilters =
                if (tabIndex == MOVIE_INDEX) currentState.movieFilters else currentState.tvFilters
            val updatedFilters = currentFilters.copy(imdbRating = newRating)

            currentState.copy(
                movieFilters = if (tabIndex == MOVIE_INDEX) updatedFilters else currentState.movieFilters,
                tvFilters = if (tabIndex == TV_SHOW_INDEX) updatedFilters else currentState.tvFilters
            )
        }
    }

    override fun onClearFilters(tabIndex: Int) {
        updateState {
            FilterUiState(
                tvGenres = it.tvGenres,
                movieGenres = it.movieGenres
            )
        }
        tryToExecute(
            callee = { clearSelectedFilters(tabIndex) }
        )
    }


    override fun onApplyClicked(tabIndex: Int) {
        tryToExecute(
            callee = { emitSelectedFilters(tabIndex) }
        )
    }

    private fun fetchTvShowGenres() {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            callee = ::loadTvShowGenres
        )
    }

    private fun fetchMovieGenres() {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            callee = ::loadMovieGenres
        )
    }

    private suspend fun emitSelectedFilters(tabIndex: Int) {
        val currentState = state.value
        val currentFilters =
            if (tabIndex == MOVIE_INDEX) currentState.movieFilters else currentState.tvFilters

        val mediaFilters = MediaFilters(
            startYear = currentFilters.yearRange.start.toInt(),
            endYear = currentFilters.yearRange.endInclusive.toInt(),
            genres = currentFilters.selectedGenres.map { it.toDomain() },
            imdbRating = currentFilters.imdbRating.toFloat()
        )
        _filterResult.emit(tabIndex to mediaFilters)
    }

    private suspend fun clearSelectedFilters(tabIndex: Int) {
        updateState { currentState ->
            val clearedFilters = MediaTabFilters()
            currentState.copy(
                movieFilters = if (tabIndex == MOVIE_INDEX) clearedFilters else currentState.movieFilters,
                tvFilters = if (tabIndex == TV_SHOW_INDEX) clearedFilters else currentState.tvFilters
            )
        }
        val currentFilters = MediaFilters(
            startYear = 1850,
            endYear = 2025,
            genres = emptyList(),
            imdbRating = 0f
        )
        _filterResult.emit(tabIndex to currentFilters)
    }

    private suspend fun loadTvShowGenres() {
        val tvShowGenres = manageTvSeriesUseCase.getSeriesGenres()
        updateState {
            it.copy(
                tvGenres = tvShowGenres.map { it.toState() },
                isLoading = false
            )
        }

    }


    private suspend fun loadMovieGenres() {
        val movieGenres = manageMovieUseCase.getMovieGenres()
        updateState {
            it.copy(
                movieGenres = movieGenres.map { it.toState() },
                isLoading = false
            )
        }
    }


    companion object {
        const val MOVIE_INDEX = 0
        const val TV_SHOW_INDEX = 1
    }
}