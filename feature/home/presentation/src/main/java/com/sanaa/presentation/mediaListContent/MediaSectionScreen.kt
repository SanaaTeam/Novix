package com.sanaa.presentation.mediaListContent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sanaa.api.StartRoute
import com.sanaa.designsystem.design_system.component.top_bar.NovixTopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.feature.home.presentation.R
import com.sanaa.presentation.components.MediaListSectionContent
import com.sanaa.presentation.model.MediaItem
import com.sanaa.presentation.model.MediaType
import org.koin.androidx.compose.koinViewModel


@Composable
fun MediaSectionScreen(
    onMediaClick: (startRoute: StartRoute, id: Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MediaListScreenViewModel = koinViewModel<MediaListScreenViewModel>(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is MediaListScreenEffect.NavigateToMediaDetails -> {
                    if (effect.mediaType == MediaType.TV_SHOW) {
                        onMediaClick(StartRoute.SERIES, effect.id)
                    } else if (effect.mediaType == MediaType.MOVIE) {
                        onMediaClick(StartRoute.MOVIE, effect.id)
                    }
                }

                is MediaListScreenEffect.NavigateBack -> { /* TODO() */ }
            }
        }
    }

    MediaListScreenContent(
        state = state.value,
        interactionListener = viewModel,
        modifier = modifier,
    )
}

@Composable
fun MediaListScreenContent(
    state: MediaListScreenUiState,
    interactionListener: MediaListScreenInteractionListener,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        NovixTopBar(
            leftContent = {
                TopBarClickableIcon(
                    icon = painterResource(id = R.drawable.icon_back),
                    onClick = interactionListener::onBackClick
                )
            },
            screenTitle = state.title,
            modifier = Modifier
                .fillMaxWidth()
                .systemBarsPadding()
        )

        MediaListSectionContent(
            genres = state.genreList,
            mediaList = state.mediaList,
            selectedGenreId = state.selectedGenreId,
            onGenreClick = interactionListener::onGenreClick,
            onMediaClick = interactionListener::onMediaClick,
            onSaveIconClick = interactionListener::onSaveIconClick,
        )
    }
}

@Preview
@Composable
fun MediaListScreenContentPreview() {

    var state by remember {
        mutableStateOf<MediaListScreenUiState>(
            MediaListScreenUiState(
                title = "Top Rated",
                genreList = emptyList(),
                mediaList = emptyList(),
                selectedGenreId = null
            )
        )
    }
    MediaListScreenContent(
        state = state,
        interactionListener = object : MediaListScreenInteractionListener {
            override fun onGenreClick(id: Int?) {
                state = state.copy(selectedGenreId = id)
            }

            override fun onMediaClick(media: MediaItem) {}
            override fun onSaveIconClick(media: MediaItem) {}
            override fun onBackClick() {}
        }
    )
}

