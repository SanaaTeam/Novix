package com.sanaa.presentation.screen.trendingMediaScreen.trendingMoviesScreen

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sanaa.api.StartRoute
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.feature.home.presentation.R
import com.sanaa.presentation.screen.trendingMediaScreen.TrendingMediaScreenEffect
import com.sanaa.presentation.screen.trendingMediaScreen.screenContent.TrendingMediaScreenContent
import org.koin.androidx.compose.koinViewModel


@Composable
fun TrendingMoviesScreen(
    onMediaClick: (startRoute: StartRoute, id: Int) -> Unit,
    modifier: Modifier = Modifier,
//    navController: NavController,
    viewModel: TrendingMoviesScreenViewModel = koinViewModel<TrendingMoviesScreenViewModel>(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is TrendingMediaScreenEffect.NavigateToMediaDetails -> {
                    onMediaClick(StartRoute.MOVIE, effect.id)
                }

                is TrendingMediaScreenEffect.NavigateBack -> {
//                    navController.popBackStack()
                }
            }
        }
    }
    NovixTheme(isSystemInDarkTheme()) {
        TrendingMediaScreenContent(
            title = stringResource(R.string.trending_movies),
            state = state.value,
            interactionListener = viewModel,
            modifier = modifier,
        )
    }
}
