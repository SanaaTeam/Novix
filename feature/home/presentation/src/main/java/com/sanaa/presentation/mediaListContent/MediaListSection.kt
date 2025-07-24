package com.sanaa.presentation.mediaListContent

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sanaa.designsystem.design_system.component.chips.NovixToggleableChip
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.home.presentation.R
import com.sanaa.presentation.components.MediaListGrid
import com.sanaa.presentation.model.GenreUiState
import com.sanaa.presentation.model.MediaItem
import com.sanaa.presentation.model.MediaType
import org.koin.androidx.compose.koinViewModel


@Composable
fun MediaListSection(
    modifier: Modifier = Modifier,
    viewModel: MediaListSectionViewModel = koinViewModel<MediaListSectionViewModel>(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
//    val navController = LocalNavControllerProvider.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is MediaListContentEffect.NavigateToMediaDetails -> { /* TODO() */ }
            }
        }
    }

    MediaListSectionContent(
        state = state.value,
        interactionListener = viewModel,
        modifier = modifier,
    )
}

@Composable
fun MediaListSectionContent(
    state: MediaListSectionUiState,
    interactionListener: MediaListSectionInteractionListener,
    modifier: Modifier = Modifier,
    isScrollEnabled: Boolean = true,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                NovixToggleableChip(
                    text = stringResource(R.string.all),
                    onClick = {
                        interactionListener.onGenreClick(null)
                    },
                    isSelected = state.selectedGenreId == null,
                )
            }
            items(state.genreList, key = { it.id }) { genre ->
                NovixToggleableChip(
                    text = genre.name,
                    onClick = { interactionListener.onGenreClick(genre.id) },
                    isSelected = genre.id == state.selectedGenreId,
                )
            }
        }
        MediaListGrid(
            mediaList = state.mediaList,
            onMediaClick = interactionListener::onMediaClick,
            isScrollEnabled = isScrollEnabled,
        )
    }
}

@PreviewLightDark
@Composable
private fun MediaListSectionPreview() {

    var state = MediaListSectionUiState(
        genreList = listOf(
            GenreUiState(id = 1, name = "Action"),
            GenreUiState(id = 2, name = "Comedy"),
            GenreUiState(id = 3, name = "Drama"),
        ),
        mediaList = listOf(
            MediaItem(
                id = 1,
                title = "Movie 1",
                imageUrl = "",
                rating = 8.5f,
                mediaType = MediaType.MOVIE
            ),
            MediaItem(
                id = 2,
                title = "Movie 2",
                imageUrl = "",
                rating = 7.0f,
                mediaType = MediaType.MOVIE
            ),
            MediaItem(
                id = 3,
                title = "Tv Show 3",
                imageUrl = "",
                rating = 9.0f,
                mediaType = MediaType.TV_SHOW
            )
        ),
        selectedGenreId = null
    )
    val interactionListener = object : MediaListSectionInteractionListener {
        override fun onGenreClick(genreId: Int?) {
            state = state.copy(selectedGenreId = genreId)
        }

        override fun onMediaClick(media: MediaItem) {

        }
    }
    NovixTheme(isSystemInDarkTheme()) {
        MediaListSectionContent(
            state= state,
            interactionListener = interactionListener,
            modifier = Modifier.background(Theme.colors.surface)
        )
    }
}