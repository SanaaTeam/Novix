package com.sanaa.presentation.filter_bottomsheet.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R as DesignSystemR
import com.sanaa.designsystem.design_system.component.chips.CategoryChip
import com.sanaa.designsystem.design_system.theme.Theme
import entity.Genre
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GenreChips(
    genres: List<Genre>,
    selectedGenres: Set<Genre>,
    onGenreSelected: (Genre) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(DesignSystemR.string.genres),
            style = Theme.textStyle.title.small,
            color = Theme.colors.title
        )

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            genres.forEach { genre ->
                CategoryChip(
                    text = genre.toReadableString(),
                    isSelected = (genre in selectedGenres),
                    onClick = { onGenreSelected(genre) }
                )
            }
        }
    }
}

private fun Genre.toReadableString(): String {
    return this.name.replace('_', ' ').lowercase(Locale.getDefault())
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun GenreChipsPreview() {
    val genres = Genre.entries
    var selectedGenres by remember { mutableStateOf(setOf(Genre.ANIMATION)) }

    Column(modifier = Modifier.padding(16.dp)) {
        GenreChips(
            genres = genres,
            selectedGenres = selectedGenres,
            onGenreSelected = { genre ->
                selectedGenres = if (genre in selectedGenres) {
                    selectedGenres - genre
                } else {
                    selectedGenres + genre
                }
            }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun GenreChipsPreviewNoGenresSelected() {
    val genres = Genre.entries
    Column(modifier = Modifier.padding(16.dp)) {
        GenreChips(genres = genres, selectedGenres = emptySet(), onGenreSelected = {})
    }
}

