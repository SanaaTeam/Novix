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
import com.sanaa.designsystem.design_system.component.chips.CategoryChip
import com.sanaa.designsystem.design_system.theme.Theme
import entity.Genre
import com.sanaa.presentation.R

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
            text = stringResource(R.string.genres),
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
        Genre.DRAMA -> stringResource(R.string.genre_drama)
        Genre.COMEDY -> stringResource(R.string.genre_comedy)
        Genre.ADVENTURE -> stringResource(R.string.genre_adventure)
        Genre.ACTION -> stringResource(R.string.genre_action)
        Genre.ROMANCE -> stringResource(R.string.genre_romance)
        Genre.FANTASY -> stringResource(R.string.genre_fantasy)
        Genre.SCIENCE_FICTION -> stringResource(R.string.genre_science_fiction)
        Genre.HORROR -> stringResource(R.string.genre_horror)
        Genre.CRIME -> stringResource(R.string.genre_crime)
        Genre.ANIMATION -> stringResource(R.string.genre_animation)
        Genre.DOCUMENTARY -> stringResource(R.string.genre_documentary)
        Genre.THRILLER -> stringResource(R.string.genre_thriller)
        Genre.MUSIC -> stringResource(R.string.genre_music)
        Genre.MYSTERY -> stringResource(R.string.genre_mystery)
        Genre.KIDS -> stringResource(R.string.genre_kids)
        Genre.HISTORY -> stringResource(R.string.genre_history)
        Genre.FAMILY -> stringResource(R.string.genre_family)
        Genre.WAR -> stringResource(R.string.genre_war)
        Genre.TALK -> stringResource(R.string.genre_talk)
        Genre.SOAP -> stringResource(R.string.genre_soap)
        Genre.REALITY -> stringResource(R.string.genre_reality)
        Genre.NEWS -> stringResource(R.string.genre_news)
        Genre.TV_MOVIE -> stringResource(R.string.genre_tv_movie)
        Genre.WESTERN -> stringResource(R.string.genre_western)
        Genre.WAR_AND_POLITICS -> stringResource(R.string.genre_war_and_politics)
        Genre.SCI_FI_AND_FANTASY -> stringResource(R.string.genre_sci_fi_and_fantasy)
        Genre.ACTION_AND_ADVENTURE -> stringResource(R.string.genre_action_and_adventure)
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

