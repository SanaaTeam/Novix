package com.sanaa.presentation.screen.actor.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sanaa.designsystem.design_system.component.loading.NovixLoadingIndicator
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixBackgroundShapes
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.top_bar.AppTopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.presentation.R
import com.sanaa.presentation.screen.actor.ActorScreenUiState
import com.sanaa.presentation.screen.actor.ActorViewModel
import com.sanaa.presentation.screen.actor.componants.GalleryCard
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ActorGalleryScreen(
    actorId: Int,
    navigateBack: () -> Unit,
    viewModel: ActorViewModel = koinViewModel { parametersOf(actorId) }
) {
    BackHandler(onBack = navigateBack)

    val uiState by viewModel.state.collectAsStateWithLifecycle()

    NovixTheme(isDarkMode = isSystemInDarkTheme()) {
        ActorGalleryContent(
            state = uiState,
            onBackClick = navigateBack,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ActorGalleryContent(
    state: ActorScreenUiState,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {
    NovixScaffold(
        backgroundShapes = { NovixBackgroundShapes() },
    ) {
        Column(
            modifier = modifier
        ) {
            AppTopBar(
                leftContent = {
                    TopBarClickableIcon(
                        icon = painterResource(id = R.drawable.icon_back),
                        onClick = onBackClick
                    )
                },
                screenTitle = stringResource(com.sanaa.presentation.R.string.gallery),
                modifier = Modifier
                    .fillMaxWidth()
                    .systemBarsPadding()
            )

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

                )  { loading ->
                    if (loading) {
                        NovixLoadingIndicator()
                    } else {
                        LazyVerticalGrid(
                            modifier = Modifier.fillMaxSize(),
                            columns = GridCells.Adaptive(minSize = 104.dp),
                            contentPadding = PaddingValues(
                                start = 16.dp, end = 16.dp, bottom = 16.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(
                                12.dp
                            ),
                            horizontalArrangement = Arrangement.spacedBy(
                                8.dp
                            )
                        ) {
                            items(state.galleryImageUrls) { image ->
                                GalleryCard(image, modifier = Modifier.height(101.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
