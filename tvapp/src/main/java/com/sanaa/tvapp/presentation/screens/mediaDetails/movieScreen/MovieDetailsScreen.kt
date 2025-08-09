package com.sanaa.tvapp.presentation.screens.mediaDetails.movieScreen

import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.material3.Text
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.tvapp.presentation.screens.mediaDetails.components.DetailsHeaderSection
import com.sanaa.tvapp.presentation.screens.mediaDetails.components.DetailsTopBar
import com.sanaa.tvapp.presentation.screens.mediaDetails.components.TrailerAndRateSection

@Composable
fun MovieDetailsScreen(
    modifier: Modifier = Modifier,
    movieDetailsViewModel:MovieDetailsViewModel = hiltViewModel()
) {
    val state = movieDetailsViewModel.state.collectAsState()

    Text(
        text = state.value.movieDetails.title
    )
    Log.d("test99", "MovieDetailsScreen:${state.value} ")
//        MovieDetailsContent()
}

@Composable
fun MovieDetailsContent(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    Column (
        modifier
            .verticalScroll(scrollState)
            .fillMaxSize()
            .padding(top = 40.dp)

    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight * 0.7f)
        ){
            DetailsHeaderSection(
                backgroundImageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTtSj7YAlBbu9_9xwuoELHeiYEHsBfnbwd8EQ&s",
                title = "Breaking Bad",
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = "2023 • 5 Seasons • TV-MA",
                        style = Theme.textStyle.body.small,
                        color = Theme.colors.body
                    )
                    Text(
                        text = "A high school chemistry teacher turned methamphetamine manufacturer faces moral dilemmas and dangerous adversaries.",
                        style = Theme.textStyle.body.small,
                        color = Theme.colors.body
                    )
                    TrailerAndRateSection()
                }
            }
            DetailsTopBar(onBackClick = {})
        }


    }
}

@Preview(device = Devices.TV_1080p, showBackground = true)
@Composable
fun DetailsPreview(modifier: Modifier = Modifier) {
    NovixTheme(isSystemInDarkTheme()) {
        MovieDetailsScreen()
    }
}