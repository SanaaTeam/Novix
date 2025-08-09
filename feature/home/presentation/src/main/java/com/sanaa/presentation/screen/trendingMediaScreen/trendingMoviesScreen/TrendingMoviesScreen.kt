package com.sanaa.presentation.screen.trendingMediaScreen.trendingMoviesScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sanaa.api.MediaDetailsApi
import com.sanaa.api.StartRoute
import com.sanaa.api.launchAuthActivityForResult
import com.sanaa.feature.home.presentation.R
import com.sanaa.presentation.api.navigation.LocalAppNavController
import com.sanaa.presentation.bottomsheet.addEditBookmark.AddBookmarkListBottomSheet
import com.sanaa.presentation.bottomsheet.saveToListBottomsheet.SaveToListBottomSheet
import com.sanaa.presentation.components.SnackData
import com.sanaa.presentation.navigation.HomeApiEntryPoint
import com.sanaa.presentation.screen.trendingMediaScreen.TrendingMediaScreenEffect
import com.sanaa.presentation.screen.trendingMediaScreen.screenContent.TrendingMediaScreenContent
import dagger.hilt.android.EntryPointAccessors

@Composable
fun TrendingMoviesScreen(
    modifier: Modifier = Modifier,
    viewModel: TrendingMoviesScreenViewModel = hiltViewModel()
) {
    val navController = LocalAppNavController.current
    val appContext = LocalContext.current.applicationContext
    var snack by remember { mutableStateOf<SnackData?>(null) }


    val detailsApi: MediaDetailsApi = remember {
        EntryPointAccessors
            .fromApplication(appContext, HomeApiEntryPoint::class.java)
            .detailsApi()
    }
    val context = LocalContext.current

    val authApi = EntryPointAccessors.fromApplication(
        context,
        HomeApiEntryPoint::class.java
    ).authenticationApi()

    val launcher = launchAuthActivityForResult()

    val state = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is TrendingMediaScreenEffect.NavigateToMediaDetails -> {
                    detailsApi.launch(
                        context = navController.context,
                        id = effect.id,
                        startRoute = StartRoute.MOVIE
                    )
                }

                is TrendingMediaScreenEffect.NavigateBack -> {
                    navController.popBackStack()
                }

                is TrendingMediaScreenEffect.NavigateToLogin -> {
                    launcher.launch(authApi.getLaunchIntent(context))
                }

                is TrendingMediaScreenEffect.ShowError -> {
                    snack = SnackData(message = effect.message, isError = true)
                }

                is TrendingMediaScreenEffect.ShowSuccess -> {
                    snack = SnackData(message = effect.message, isError = false)
                }
            }
        }
    }
    TrendingMediaScreenContent(
        title = stringResource(R.string.trending_movies),
        state = state.value,
        interactionListener = viewModel,
        modifier = modifier,
    )

    if (state.value.showSaveToListBottomSheet && state.value.selectedMediaId != null) {
        SaveToListBottomSheet(
            isVisible = state.value.showSaveToListBottomSheet,
            mediaId = state.value.selectedMediaId!!.toLong(),
            onDismiss = viewModel::onDismissSaveToListBottomSheet,
            onCreateNewListClick = viewModel::onCreateNewListClick,
        )

        Box(modifier = Modifier.systemBarsPadding()) {

            TrendingMediaScreenContent(
                title = stringResource(R.string.trending_movies),
                state = state.value,
                interactionListener = viewModel,
                modifier = modifier,
            )


            AddBookmarkListBottomSheet(
                isVisible = state.value.showAddListBottomSheet,
                onDismiss = viewModel::onDismissAddListBottomSheet,
                mediaId = state.value.selectedMediaId ?: 0
            )
        }
    }
}

