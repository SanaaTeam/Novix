package com.sanaa.presentation.screen.actor.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sanaa.designsystem.design_system.component.loading.LoadingIndicator
import com.sanaa.designsystem.design_system.component.novix_scaffold.BackgroundShapes
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.feature.mediadetails.presentation.R
import com.sanaa.presentation.screen.actor.ActorScreenUiState
import com.sanaa.presentation.screen.actor.ActorViewModel
import com.sanaa.presentation.screen.actor.componants.GalleryCard
import com.sanaa.designsystem.R as designR

@Composable
fun ActorGalleryScreen(
    navigateBack: () -> Unit,
    viewModel: ActorViewModel = hiltViewModel(),
) {
    BackHandler(onBack = navigateBack)

    val uiState by viewModel.state.collectAsStateWithLifecycle()

    ActorGalleryContent(
        state = uiState,
        onBackClick = navigateBack,
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun ActorGalleryContent(
    state: ActorScreenUiState,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {
    NovixScaffold(backgroundShapes = { BackgroundShapes() }) {
        Column(
            modifier = modifier.navigationBarsPadding()
        ) {
            ActorGalleryTopBar(onBackClick)

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(
                    state.isLoading,
                    modifier = Modifier.align(Alignment.Center),
                    contentAlignment = Alignment.Center

                ) { loading ->
                    if (loading)
                        LoadingIndicator()
                    else
                        GalleryImages(state)
                }
            }
        }
    }
}

@Composable
private fun ActorGalleryTopBar(onBackClick: () -> Unit) {
    TopBar(
        leftContent = {
            TopBarClickableIcon(
                icon = painterResource(id = designR.drawable.icon_back),
                onClick = onBackClick
            )
        },
        screenTitle = stringResource(R.string.gallery),
        modifier = Modifier
            .fillMaxWidth()
            .systemBarsPadding()
    )
}

@Composable
private fun GalleryImages(state: ActorScreenUiState) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Adaptive(minSize = 104.dp),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = state.galleryImageUrls,
            key = { state.galleryImageUrls }
        ) { image ->
            GalleryCard(image, modifier = Modifier.aspectRatio(1f))
        }
    }
}
