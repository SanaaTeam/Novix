package com.sanaa.presentation.screen.trendingMediaScreen.trendingTvShowScreen

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
import com.sanaa.presentation.api.navigation.AppNavigation
import com.sanaa.presentation.screen.trendingMediaScreen.TrendingMediaScreenEffect
import com.sanaa.presentation.screen.trendingMediaScreen.screenContent.TrendingMediaScreenContent
import org.koin.androidx.compose.koinViewModel
import org.koin.java.KoinJavaComponent.inject


@Composable
fun TrendingTvShowsScreen(
    modifier: Modifier = Modifier,
    viewModel: TrendingTvShowsScreenViewModel = koinViewModel<TrendingTvShowsScreenViewModel>(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val navController = AppNavigation.app
    val detailsApi: MediaDetailsApi by inject(
        MediaDetailsApi::class.java
    )

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is TrendingMediaScreenEffect.NavigateToMediaDetails -> {
                    detailsApi.launch(
                        context = navController.context,
                        id = effect.id,
                        startRoute = StartRoute.SERIES
                    )
                }

                is TrendingMediaScreenEffect.NavigateBack -> {
                    navController.popBackStack()
                }
            }
        }
    }

    NovixTheme(isSystemInDarkTheme()) {
        TrendingMediaScreenContent(
            title = stringResource(R.string.trending_tvshows),
            state = state.value,
            interactionListener = viewModel,
            modifier = modifier,
            onLoading = { viewModel.onLoading() },
            onRetryClick = { viewModel.onRetryClick() },
        )
    }
}