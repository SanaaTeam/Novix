package com.sanaa.presentation.screen.trendingPeopleScreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.sanaa.api.MediaDetailsApi
import com.sanaa.api.StartRoute
import com.sanaa.designsystem.design_system.component.loading.LoadingIndicator
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.screen_state_content.NetworkDisconnectionContact
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.feature.home.presentation.R
import com.sanaa.presentation.api.HomeApiEntryPoint
import com.sanaa.presentation.app.navigation.LocalMainNavController
import com.sanaa.presentation.components.NovixAnimatedSnackBarHost
import com.sanaa.presentation.components.RefreshButton
import com.sanaa.presentation.components.lists.PersonList
import com.sanaa.presentation.providers.LocalThemeProvider
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import com.sanaa.designsystem.R as designSystemR

@Composable
fun TrendingPeopleScreen(
    viewModel: TrendingPeopleScreenViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    EffectHandler(viewModel.effect)

    TrendingPeopleScreenContent(
        state = state.value,
        interactionListener = viewModel
    )
}

@Composable
private fun TrendingPeopleScreenContent(
    state: TrendingPeopleScreenUiState,
    interactionListener: TrendingPeopleScreenInteractionListener,
) {
    val people = state.people.collectAsLazyPagingItems()

    NovixScaffold(
        topBar = {
            TopBar(
                leftContent = {
                    TopBarClickableIcon(
                        icon = painterResource(id = designSystemR.drawable.icon_back),
                        onClick = interactionListener::onBackClick
                    )
                },
                screenTitle = stringResource(id = R.string.trending_people),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .statusBarsPadding()
            )
        },
        snackBarHost = {
            NovixAnimatedSnackBarHost(
                data = state.snackBarData,
                onDismiss = interactionListener::onSnackBarDismiss,
                modifier = Modifier.statusBarsPadding()
            )
        },
    ) {
        AnimatedContent(
            targetState = state.isNoInternetConnection && (people.itemCount == 0),
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize(),
        ) { showNoInternetScreen ->
            when {
                showNoInternetScreen -> {
                    NetworkDisconnectionContact(
                        onRetryClick = interactionListener::onRetryClick,
                        useDarkTheme = LocalThemeProvider.current,
                    )
                }

                people.loadState.refresh is LoadState.Loading && (people.itemCount == 0) -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        LoadingIndicator()
                    }
                }

                else -> {
                    PersonList(
                        persons = people,
                        onItemClick = interactionListener::onActorClick
                    )
                    if (people.loadState.hasError) {
                        RefreshButton(onRetryClick = interactionListener::onRetryClick)
                    }
                }
            }
        }
    }
}

@Composable
private fun EffectHandler(
    effect: SharedFlow<TrendingPeopleScreenEffect>,
) {
    val navController = LocalMainNavController.current
    val appContext = LocalContext.current.applicationContext

    val detailsApi: MediaDetailsApi = remember {
        EntryPointAccessors
            .fromApplication(appContext, HomeApiEntryPoint::class.java)
            .detailsApi()
    }

    LaunchedEffect(Unit) {
        effect.collectLatest { effect ->
            when (effect) {
                is TrendingPeopleScreenEffect.NavigateBack -> {
                    navController.popBackStack()
                }

                is TrendingPeopleScreenEffect.NavigateToActorDetails -> {
                    detailsApi.launch(
                        context = navController.context,
                        id = effect.actorId,
                        startRoute = StartRoute.ACTOR
                    )
                }
            }
        }
    }
}