package com.sanaa.presentation.screen.componants

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.cards.MovieSeriesPosterCard
import com.sanaa.designsystem.design_system.component.chips.SaveIconChip
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun SearchHistoryContent(
    onClearRecentViewClicked: () -> Unit = {},
    onClearRecentSearchClicked: () -> Unit = {},
    onCancelClicked: () -> Unit = {},
    recentSearches: List<String> = emptyList(),
    recentViewed: List<Int> = emptyList(),
) {
    Column(modifier = Modifier.padding(top = 12.dp)) {

        SectionHeader(
            title = stringResource(R.string.recent_viewed),
            actionText = stringResource(R.string.clear_all),
            onActionClick = onClearRecentViewClicked
        )
        LazyRow(
            modifier = Modifier
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(recentViewed) {index, item ->
                MovieSeriesPosterCard(
                    poster = painterResource(id =item),
                    topLeftContent = {
                        SaveIconChip(
                            onClick = {}
                        )
                    },
                )
            }
        }
        SectionHeader(
            title = stringResource(R.string.recent_search),
            actionText = stringResource(R.string.clear_all),
            onActionClick = onClearRecentSearchClicked
        )
        LazyColumn ( modifier = Modifier
            .padding(bottom = 38.dp)){
            itemsIndexed(recentSearches) { index, item ->
                RecentSearchItem(
                    text = item,
                    isLast = index == recentSearches.lastIndex,
                    onCancelClicked = onCancelClicked
                )
            }
        }
    }


}

@Composable
fun SectionHeader(
    title: String,
    actionText: String,
    onActionClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 12.dp)
    ) {
        Text(
            text = title,
            style = Theme.textStyle.label.medium,
            color = Theme.colors.body,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = actionText,
            style = Theme.textStyle.label.medium,
            color = Theme.colors.primary,
            modifier = Modifier.clickable(onClick = onActionClick)
        )
    }
}


val recentViewedList = listOf(
    R.drawable.movie_poster,
    R.drawable.movie_poster1,
    R.drawable.movie_poster2,
    R.drawable.movie_poster3,
    R.drawable.movie_poster,
    R.drawable.movie_poster1,
    R.drawable.movie_poster2,
    R.drawable.movie_poster3,
    R.drawable.movie_poster,
    R.drawable.movie_poster1,
    R.drawable.movie_poster2,
    R.drawable.movie_poster3,
)
val recentSearchesList = listOf(
    "Shutter island", "Inception", "Tenet",
    "Memento", "Shutter island", "Inception",
    "Tenet", "Memento","Shutter island", "Inception", "Tenet",
    "Memento", "Shutter island", "Inception",
    "Tenet", "Memento"
)

@Preview(showBackground = true)
@Composable
private fun SearchPreview() {
    SearchHistoryContent(
        recentSearches = recentSearchesList,
        recentViewed = recentViewedList
    )
}