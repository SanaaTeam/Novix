package com.sanaa.presentation.screens.actors.screen

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.cards.MovieSeriesPosterCard
import com.sanaa.designsystem.design_system.component.chips.SaveIconChip
import com.sanaa.designsystem.design_system.component.top_bar.AppTopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.image_viewer.component.RemoteCensoredImageViewer
import com.sanaa.presentation.screens.actors.ActorScreenUiState
import com.sanaa.presentation.screens.actors.ActorViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun TopMoviesScreen(
    actorId: Int,
    navigateBack: () -> Unit,
) {
    BackHandler(onBack = navigateBack)

    val viewModel: ActorViewModel =
        koinViewModel { parametersOf(actorId) }
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    NovixTheme(isSystemInDarkTheme()) {

        TopMoviesContent(
            state = uiState,
//            listener = viewModel,
            modifier = Modifier.fillMaxSize(),
            onBackClick = navigateBack
        )
    }
}

@Composable
fun TopMoviesContent(
    state: ActorScreenUiState,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 140.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        item {
            AppTopBar(
                leftContent = {
                    TopBarClickableIcon(
                        icon = painterResource(id = R.drawable.icon_arrow_back),
                        onClick = onBackClick
                    )
                },
                modifier = Modifier.padding(top = 52.dp)
            )
        }

        items(state.topMovies) { movie ->
            Log.d("MoviesContent", "Movie: $movie")
            MovieSeriesPosterCard(boastImage = {
                RemoteCensoredImageViewer(
                    imageUrl = movie.imageUrl ?: "",
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop,
                    blurRadius = 150,
                    sfwThreshold = 0.75f,
                    nsfwThreshold = 0.15f,
                    //                    placeholder = painterResource(placeholderResId),
                    //                    error = painterResource(placeholderResId),
                    contentDescription = movie.title,
                    placeholderBackgroundColor = Theme.colors.surface,
                    hintText = stringResource(R.string.clear),
                    textStyle = Theme.textStyle.body.small,
                    iconSize = 24.dp,
                )
            }, topLeftContent = {
                SaveIconChip(onClick = {})
            }, onCardClick = {}
            )
        }
    }

//    if (state.isLoading) {
//        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//            WavyProgressIndicator()
//        }
//    } else {
//        Column(
//            modifier = modifier.padding(16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text("Top-movie screen")
//            Button(onClick = onBackClick) {             // ← already fine
//                Text("Back")
//            }
//        }
//    }
}
