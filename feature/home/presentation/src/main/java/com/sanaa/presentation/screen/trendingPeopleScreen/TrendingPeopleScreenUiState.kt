package com.sanaa.presentation.screen.trendingPeopleScreen

import androidx.paging.PagingData
import com.sanaa.presentation.state.PersonUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class TrendingPeopleScreenUiState(
    val isLoading: Boolean = false,
    val people: Flow<PagingData<PersonUiState>> = flowOf(PagingData.empty()),
    val isNoInternetConnection: Boolean = false,
    val error: String? = null
)