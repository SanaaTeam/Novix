package com.sanaa.presentation.screen.mediaTabScreen.continueWatchingScreen

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sanaa.api.MediaDetailsApi
import com.sanaa.api.StartRoute
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.feature.home.presentation.R
import com.sanaa.presentation.api.navigation.LocalAppNavController
import com.sanaa.presentation.screen.mediaTabScreen.MediaTabScreenEffect
import com.sanaa.presentation.screen.mediaTabScreen.screenContent.MediaTabScreenContent
import com.sanaa.presentation.state.MediaType
import org.koin.androidx.compose.koinViewModel
import org.koin.java.KoinJavaComponent.inject


@Composable
fun TopRatedMediaScreen(
    modifier: Modifier = Modifier,
    viewModel: ContinueWatchingMediaScreenViewModel = koinViewModel<ContinueWatchingMediaScreenViewModel>(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    val detailsApi: MediaDetailsApi by inject(
        MediaDetailsApi::class.java
    )

    val navController = LocalAppNavController.current
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is MediaTabScreenEffect.NavigateToMediaDetails -> {
                    if (effect.mediaType == MediaType.MOVIE) {
                        detailsApi.launch(
                            context = navController.context,
                            id = effect.id,
                            startRoute = StartRoute.MOVIE
                        )
                    } else if (effect.mediaType == MediaType.TV_SHOW) {
                        detailsApi.launch(
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
    NovixTheme(isSystemInDarkTheme()){
        MediaTabScreenContent(
            title = stringResource(R.string.top_rated),
            state = state.value,
            interactionListener = viewModel,
            modifier = modifier,
        )
    }
}
