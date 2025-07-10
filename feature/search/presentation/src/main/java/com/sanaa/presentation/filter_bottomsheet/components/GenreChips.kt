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
import com.sanaa.presentation.R
import com.sanaa.designsystem.design_system.component.chips.CategoryChip
import com.sanaa.designsystem.design_system.theme.Theme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GenreChips(
    genres: List<String>,
    selectedGenre: String,
    onGenreSelected: (String) -> Unit,
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
                    text = genre,
                    isSelected = (genre == selectedGenre),
                    onClick = { onGenreSelected(genre) }
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun GenreChipsPreview() {
    val genres = listOf(
        "All",
        "Comedy",
        "Action",
        "Crime",
        "Adventure",
        "Animation",
        "Documentary",
        "Drama",
        "Family"
    )
    var selectedGenre by remember { mutableStateOf("Animation") }

        Column(modifier = Modifier.padding(16.dp)) {
            GenreChips(
                genres = genres,
                selectedGenre = selectedGenre,
                onGenreSelected = { genre ->
                    selectedGenre = genre
                }
            )
        }
}
