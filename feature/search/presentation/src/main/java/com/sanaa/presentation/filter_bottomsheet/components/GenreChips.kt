package com.sanaa.presentation.filter_bottomsheet.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.chips.NovixToggleableChip
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.search.presentation.R
import com.sanaa.presentation.filter_bottomsheet.state.GenreUiState

@Composable
fun GenreChips(
    genres: List<GenreUiState>,
    selectedGenres: Set<GenreUiState>,
    onGenreSelected: (GenreUiState) -> Unit,
    modifier: Modifier = Modifier,
    animateWidth: Boolean = true,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        BasicText(
            text = stringResource(R.string.genres),
            style = Theme.textStyle.title.small.copy(color = Theme.colors.title),
        )

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            genres.forEach { genre ->
                NovixToggleableChip(
                    text = genre.name.orEmpty(),
                    isSelected = (genre in selectedGenres),
                    animateWidth = animateWidth,
                    onClick = { onGenreSelected(genre) }
                )
            }
        }
    }
}


@PreviewLightDark
@Composable
fun GenreChipsPreviewNoGenresSelected() {
    val genreList = listOf(
        "Action",
        "Adventure",
        "Animation",
        "Comedy",
        "Crime",
        "Documentary",
        "Drama",
        "Family",
        "Fantasy",
        "History",
        "Horror",
        "Music",
        "Mystery",
        "Romance",
        "Science Fiction",
        "TV Movie",
        "Thriller",
        "War",
        "Western"
    )
}

