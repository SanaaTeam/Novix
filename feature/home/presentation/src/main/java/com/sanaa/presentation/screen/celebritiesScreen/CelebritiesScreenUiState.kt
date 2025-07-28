package com.sanaa.presentation.screen.celebritiesScreen

import androidx.paging.PagingData
import com.sanaa.presentation.state.PersonUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class CelebritiesScreenUiState(
    val isLoading: Boolean = false,
    val celebrities: Flow<PagingData<PersonUiState>> = flowOf(PagingData.empty()),
    val isNoInternetConnection: Boolean = false,
    val error: String? = null
)