package com.sanaa.presentation.screen.myRating.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sanaa.presentation.model.RatedMediaUiModel
import com.sanaa.presentation.screen.myRating.MediaTypeUi

@Composable
fun RatedMediaListGrid(
    onCardClick: (mediaId: Int, mediaType: MediaTypeUi) -> Unit,
    onDeleteIconClick: (mediaId: Int, mediaType: MediaTypeUi) -> Unit ,
    mediaList: List<RatedMediaUiModel>,
    modifier: Modifier = Modifier,
    isScrollEnabled: Boolean = true,
) {
    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        columns = GridCells.Adaptive(minSize = 140.dp),
        contentPadding = PaddingValues(start = 12.dp, end = 12.dp, bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        userScrollEnabled = isScrollEnabled
    ) {
        items(
            items = mediaList,
            key = { mediaItem -> "${mediaItem.mediaType}_${mediaItem.id}" }
        ) { mediaItem ->
            RatedMediaItem(
                onCardClick = onCardClick,
                onDeleteClick = onDeleteIconClick,
                media = mediaItem,
            )
        }
    }
}