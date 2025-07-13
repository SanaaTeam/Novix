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
                    text = genre.toLocalizedString(),
                    isSelected = (genre in selectedGenres),
                    onClick = { onGenreSelected(genre) }
                )
            }
        }
    }
}

@Composable
private fun Genre.toLocalizedString(): String {
    return when (this) {
        Genre.DRAMA -> stringResource(DesignSystemR.string.genre_drama)
        Genre.COMEDY -> stringResource(DesignSystemR.string.genre_comedy)
        Genre.ADVENTURE -> stringResource(DesignSystemR.string.genre_adventure)
        Genre.ACTION -> stringResource(DesignSystemR.string.genre_action)
        Genre.ROMANCE -> stringResource(DesignSystemR.string.genre_romance)
        Genre.FANTASY -> stringResource(DesignSystemR.string.genre_fantasy)
        Genre.SCIENCE_FICTION -> stringResource(DesignSystemR.string.genre_science_fiction)
        Genre.HORROR -> stringResource(DesignSystemR.string.genre_horror)
        Genre.CRIME -> stringResource(DesignSystemR.string.genre_crime)
        Genre.ANIMATION -> stringResource(DesignSystemR.string.genre_animation)
        Genre.DOCUMENTARY -> stringResource(DesignSystemR.string.genre_documentary)
        Genre.THRILLER -> stringResource(DesignSystemR.string.genre_thriller)
        Genre.MUSIC -> stringResource(DesignSystemR.string.genre_music)
        Genre.MYSTERY -> stringResource(DesignSystemR.string.genre_mystery)
        Genre.KIDS -> stringResource(DesignSystemR.string.genre_kids)
        Genre.HISTORY -> stringResource(DesignSystemR.string.genre_history)
        Genre.FAMILY -> stringResource(DesignSystemR.string.genre_family)
        Genre.WAR -> stringResource(DesignSystemR.string.genre_war)
        Genre.TALK -> stringResource(DesignSystemR.string.genre_talk)
        Genre.SOAP -> stringResource(DesignSystemR.string.genre_soap)
        Genre.REALITY -> stringResource(DesignSystemR.string.genre_reality)
        Genre.NEWS -> stringResource(DesignSystemR.string.genre_news)
        Genre.TV_MOVIE -> stringResource(DesignSystemR.string.genre_tv_movie)
        Genre.WESTERN -> stringResource(DesignSystemR.string.genre_western)
        Genre.WAR_AND_POLITICS -> stringResource(DesignSystemR.string.genre_war_and_politics)
        Genre.SCI_FI_AND_FANTASY -> stringResource(DesignSystemR.string.genre_sci_fi_and_fantasy)
        Genre.ACTION_AND_ADVENTURE -> stringResource(DesignSystemR.string.genre_action_and_adventure)
    }
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

