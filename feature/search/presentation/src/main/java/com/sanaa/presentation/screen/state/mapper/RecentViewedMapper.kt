package com.sanaa.presentation.screen.state.mapper

import com.sanaa.presentation.screen.state.RecentViewedUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import search.usecase.search_param.RecentViewedMedia

fun RecentViewedMedia.toUiState() = RecentViewedUiModel(
    id = this.id,
    imageUrl = this.posterImageUrl,
    mediaType = this.mediaType.name,
    isSaved = this.isSaved,
)

fun List<RecentViewedMedia>.toUiState() = this.map { it.toUiState() }
fun Flow<List<RecentViewedMedia>>.toUiState() = this.map { it.toUiState() }
