package com.sanaa.tvapp.presentation.screens.searchScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.tv.foundation.lazy.list.TvLazyRow
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.tvapp.R
import com.sanaa.tvapp.presentation.screens.searchScreen.componants.SearchTextField
import com.sanaa.tvapp.presentation.screens.searchScreen.componants.TvMediaPosterCard
import com.sanaa.tvapp.presentation.screens.searchScreen.componants.TvTopBar

@Composable
fun SearchScreenContent(modifier: Modifier = Modifier) {
    val movies = listOf(
        "Oppenheimer",
        "Avatar 2",
        "Joker",
        "Black Panther",
        "Dune",
        "Batman Begins"
    )
    var text by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    var isSearchFieldReady by remember { mutableStateOf(false) }


    LaunchedEffect(isSearchFieldReady) {
        if (isSearchFieldReady) {
            focusRequester.requestFocus()
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colors.surface),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TvTopBar()
        SearchTextField(
            text = text,
            onTextChange = { text = it },
            modifier = Modifier
                .focusRequester(focusRequester)
                .focusable()
                .onGloballyPositioned {
                    isSearchFieldReady = true
                }
        )
        TvLazyRow(
            modifier = Modifier.padding(vertical = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(horizontal = 36.dp)
        ) {
            items(movies.size) { index ->
                TvMediaPosterCard(
                    title = movies[index],
                    PosterImage = {
                        Image(
                            painter = painterResource(id = R.drawable.img),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                )
            }
        }
    }

}