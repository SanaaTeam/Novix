package com.sanaa.tvapp.presentation.screens

import com.sanaa.tvapp.state.MediaItemUiState
import com.sanaa.tvapp.state.MediaTypeUiState

interface HomeScreenInteractionListener {
    fun onMovieGenreClick(id: Int?)
    fun onMediaClick(id: Int, mediaTypeUiState: MediaTypeUiState)
    fun onDismissSaveToListBottomSheet()
    fun onCreateNewListClick()
    fun onRetryClick()
}