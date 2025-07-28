package com.sanaa.presentation.screen.mediaTabScreen.topRatingScreen

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sanaa.api.MediaDetailsApi
import com.sanaa.api.StartRoute
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.feature.home.presentation.R
import com.sanaa.presentation.api.navigation.LocalAppNavController
import com.sanaa.presentation.screen.mediaTabScreen.MediaTabScreenEffect
import com.sanaa.presentation.screen.mediaTabScreen.screenContent.MediaTabScreenContent
import com.sanaa.presentation.state.MediaType

@Composable
fun TopRatedMediaScreen(
    modifier: Modifier = Modifier,
    viewModel: TopRatedMediaScreenViewModel = hiltViewModel(),
    mediaDetailsApi: MediaDetailsApi
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    val navController = LocalAppNavController.current
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is MediaTabScreenEffect.NavigateToMediaDetails -> {
                    if (effect.mediaType == MediaType.MOVIE) {
                        mediaDetailsApi.launch(
                            context = navController.context,
                            id = effect.id,
                            startRoute = StartRoute.MOVIE
                        )
                    } else if (effect.mediaType == MediaType.TV_SHOW) {
                        mediaDetailsApi.launch(
                            context = navController.context,
                            id = effect.id,
                            startRoute = StartRoute.SERIES
                        )
                    }
                }

                is MediaTabScreenEffect.NavigateBack -> {
                    navController.popBackStack()
                }
            }
        }
    }
    NovixTheme(isSystemInDarkTheme()) {
        MediaTabScreenContent(
            title = stringResource(R.string.top_rated),
            state = state.value,
            interactionListener = viewModel,
            modifier = modifier,
        )
    }
}
