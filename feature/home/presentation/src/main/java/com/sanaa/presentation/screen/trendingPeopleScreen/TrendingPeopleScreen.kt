package com.sanaa.presentation.screen.trendingPeopleScreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
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
import androidx.paging.compose.collectAsLazyPagingItems
import com.sanaa.api.MediaDetailsApi
import com.sanaa.api.StartRoute
import com.sanaa.designsystem.design_system.component.loading.LoadingIndicator
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.screen_state_content.NetworkDisconnectionContact
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.feature.home.presentation.R
import com.sanaa.presentation.api.navigation.LocalAppNavController
import com.sanaa.presentation.components.lists.PersonList
import com.sanaa.presentation.navigation.HomeApiEntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.collectLatest


@Composable
fun TrendingPeopleScreen(
    viewModel: TrendingPeopleViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val navController = LocalAppNavController.current
    val appContext = LocalContext.current.applicationContext

    val detailsApi: MediaDetailsApi = remember {
        EntryPointAccessors
            .fromApplication(appContext, HomeApiEntryPoint::class.java)
            .detailsApi()
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is TrendingPeopleScreenEffects.NavigateBack -> {
                    navController.popBackStack()
                }

                is TrendingPeopleScreenEffects.NavigateToActorDetails -> {
                    detailsApi.launch(
                        context = navController.context,
                        id = effect.actorId,
                        startRoute = StartRoute.ACTOR
                    )
                }
            }
        }
    }

    TrendingPeopleScreenContent(
        state = state.value, interactionListener = viewModel
    )
}

@Composable
fun TrendingPeopleScreenContent(
    state: TrendingPeopleScreenUiState,
    interactionListener: TrendingPeopleScreenInteractionListener,
) {
    val celebrities = state.people.collectAsLazyPagingItems()

    NovixScaffold(
        topBar = {
            TopBar(
                leftContent = {
                    TopBarClickableIcon(
                        icon = painterResource(id = R.drawable.icon_back),
                        onClick = interactionListener::onBackClick
                    )
                },
                screenTitle = stringResource(id = R.string.trending_people),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            )
        },
        modifier = Modifier.systemBarsPadding()

    ) {
        AnimatedContent(
            targetState = state.isLoading to state.isNoInternetConnection,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize(),
        ) { (loading, disconnected) ->
            when {
                disconnected -> {
                    NetworkDisconnectionContact(onRetryClick = interactionListener::onRetryClick)
                }

                loading -> {
                    LoadingIndicator()
                }

                else -> {
                    PersonList(
                        persons = celebrities,
                        onItemClick = interactionListener::onActorClick
                    )
                }
            }
        }
    }

}