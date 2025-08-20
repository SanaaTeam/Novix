package com.sanaa.presentation.screen.state.mapper

import com.sanaa.presentation.screen.state.RecentSearchUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import usecase.history.history_param.SearchHistory

fun SearchHistory.toUiState(): RecentSearchUiModel {
    return RecentSearchUiModel(
        id = this.id,
        title = this.query
    )
}

fun List<SearchHistory>.toUiState() = this.map { it.toUiState() }
fun Flow<List<SearchHistory>>.toUiState() = this.map { it.toUiState() }