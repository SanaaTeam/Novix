package com.sanaa.presentation.screen.componants

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.sanaa.presentation.screen.CategoryTab
import com.sanaa.presentation.screen.fake.FakeDataProvider.fakeActors
import com.sanaa.presentation.screen.fake.FakeDataProvider.fakeMovies
import com.sanaa.presentation.screen.fake.FakeDataProvider.fakeTvShows

@Composable
fun CategoryTabSection() {
    val tabs = listOf("Movies", "TV Shows", "Actors")
    var selectedTab by remember { mutableStateOf(0) }

    Column {
        CategoryTab(
            categories = tabs,
            selectedIndex = selectedTab,
            onCategorySelected = { selectedTab = it }
        )

        when (selectedTab) {
            0 -> MoviesContent(fakeMovies)
            1 -> TvShowsContent(fakeTvShows)
            2 -> ActorsContent(actors = fakeActors)
        }
    }
}