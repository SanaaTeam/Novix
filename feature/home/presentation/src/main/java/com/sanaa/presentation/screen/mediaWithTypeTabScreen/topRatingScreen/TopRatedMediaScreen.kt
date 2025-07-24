package com.sanaa.presentation.screen.mediaWithTypeTabScreen.topRatingScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.sanaa.api.StartRoute
import com.sanaa.feature.home.presentation.R
import com.sanaa.presentation.screen.mediaWithTypeTabScreen.MediaWithTypeTabScreenEffect
import com.sanaa.presentation.screen.mediaWithTypeTabScreen.screenContent.MediaWithTypeTabScreenContent
import com.sanaa.presentation.state.MediaType
import org.koin.androidx.compose.koinViewModel


@Composable
fun TopRatedMediaScreen(
    onMediaClick: (startRoute: StartRoute, id: Int) -> Unit,
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: TopRatedMediaScreenViewModel = koinViewModel<TopRatedMediaScreenViewModel>(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is MediaWithTypeTabScreenEffect.NavigateToMediaDetails -> {
                    if (effect.mediaType == MediaType.MOVIE) {
                        onMediaClick(StartRoute.MOVIE, effect.id)
                    } else if (effect.mediaType == MediaType.TV_SHOW) {
                        onMediaClick(StartRoute.SERIES, effect.id)
                    }
                }

                is MediaWithTypeTabScreenEffect.NavigateBack -> navController.popBackStack()
            }
        }
    }

    MediaWithTypeTabScreenContent(
        title = stringResource(R.string.top_rated),
        state = state.value,
        interactionListener = viewModel,
        modifier = modifier,
    )
}
