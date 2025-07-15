package com.sanaa.presentation.screen.componants

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.sanaa.designsystem.design_system.component.cards.ActorCard
import com.sanaa.presentation.screen.state.ActorUiModel
import kotlinx.coroutines.flow.Flow

@Composable
fun ActorsContent(
    actorsPagingData: Flow<PagingData<ActorUiModel>>
) {
    val lazyPagingItems = actorsPagingData.collectAsLazyPagingItems()
    
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(
            count = lazyPagingItems.itemCount,
            key = { index -> lazyPagingItems[index]?.id ?: index }
        ) { index ->
            val actor = lazyPagingItems[index]
            if (actor != null) {
                ActorCard(
                    actorName = actor.name,
                    actorImage = rememberAsyncImagePainter(actor.imageUrl),
                    playedCharacter = null
                )
            }
        }
        
        if (lazyPagingItems.loadState.refresh is LoadState.Loading) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    WavyProgressIndicator()
                }
            }
        }
        
        if (lazyPagingItems.loadState.append is LoadState.Loading) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    WavyProgressIndicator()
                }
            }
        }
    }
}
