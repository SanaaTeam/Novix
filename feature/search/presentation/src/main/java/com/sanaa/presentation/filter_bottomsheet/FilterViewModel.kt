package com.sanaa.presentation.filter_bottomsheet

import com.sanaa.presentation.base.BaseViewModel
import com.sanaa.presentation.filter_bottomsheet.state.FilterUiState
import com.sanaa.presentation.filter_bottomsheet.state.GenreUiState
import com.sanaa.presentation.screen.SearchViewModel
import com.sanaa.presentation.screen.state.mapper.toDomain
import com.sanaa.presentation.screen.state.mapper.toState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import usecase.ManageMovieUseCase
import usecase.ManageTvSeriesUseCase
import usecase.search.search_param.MediaFilters

class FilterViewModel(
    private val manageMovieUseCase: ManageMovieUseCase,
    private val manageTvSeriesUseCase: ManageTvSeriesUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<FilterUiState, Unit>(
    initialState = FilterUiState(), defaultDispatcher = dispatcher
), FilterBottomSheetInteractionsListener {
    private val _filterResult = MutableSharedFlow<MediaFilters?>()
    val filterResult = _filterResult.asSharedFlow()


    override fun onYearRangeChanged(newRange: ClosedFloatingPointRange<Float>) {
        updateState { it.copy(yearRange = newRange) }
    }

    override fun onGenreSelected(genre: GenreUiState) {
        updateState { currentState ->
            val newSelectedGenres = currentState.selectedGenres.toMutableSet().apply {
                if (contains(genre)) {
                    remove(genre)
                } else {
                    add(genre)
                }
            }
            currentState.copy(selectedGenres = newSelectedGenres)
        }
    }

    override fun onRatingChanged(newRating: Int) {
        updateState { it.copy(imdbRating = newRating) }
    }

    override fun onClearFilters() {
        updateState {
            FilterUiState(allGenres = it.allGenres)
        }
    }

    override fun onApplyClicked() {
        tryToExecute(
            callee = {
                val currentState = state.value
                val mediaFilters = MediaFilters(
                    startYear = currentState.yearRange.start.toInt(),
                    endYear = currentState.yearRange.endInclusive.toInt(),
                    genres = currentState.selectedGenres.map { it.toDomain() },
                    imdbRating = currentState.imdbRating.toFloat()
                )
                _filterResult.emit(mediaFilters)
            })
    }

    fun fetchGenresByTab(tabIndex: Int) {
        when (tabIndex) {
            SearchViewModel.MOVIE_INDEX -> fetchMovieGenres()
            SearchViewModel.TV_SHOW_INDEX -> fetchTvShowGenres()
        }
    }

    private fun fetchTvShowGenres() {

        updateState { it.copy(isLoading = true) }
        tryToExecute(
            callee = ::loadTvShowGenres
        )
    }

    private suspend fun loadTvShowGenres() {
        val tvShowGenres = manageTvSeriesUseCase.getSeriesGenres()
        updateState {
            it.copy(
                allGenres = tvShowGenres.map { it.toState() },
                isLoading = false
            )
        }

    }

    private fun fetchMovieGenres() {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            callee = ::loadMovieGenres
        )
    }

    private suspend fun loadMovieGenres() {
        val movieGenres = manageMovieUseCase.getMovieGenres()
        updateState {
            it.copy(
                allGenres = movieGenres.map { it.toState() },
                isLoading = false
            )
        }
    }
}