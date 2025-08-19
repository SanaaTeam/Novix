package com.sanaa.presentation.screen.review

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.sanaa.designsystem.design_system.component.loading.LoadingIndicator
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.screen_state_content.NetworkDisconnectionContact
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.feature.mediadetails.presentation.R
import com.sanaa.presentation.api.LocalThemeProvider
import com.sanaa.presentation.navigation.LocalNavControllerProvider
import com.sanaa.presentation.screen.review.components.EmptyReviewsContent
import com.sanaa.presentation.screen.review.components.ReviewsList
import com.sanaa.presentation.shared_component.NovixAnimatedSnackBarHost
import com.sanaa.designsystem.R as designR

@Composable
fun ReviewsScreen(
    viewModel: ReviewScreenViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val navController = LocalNavControllerProvider.current
    LaunchedEffect(Unit) {
        viewModel.effect.collect {
            when (it) {
                is ReviewScreenEffects.NavigateBack -> {
                    navController.popBackStack()
                }
            }
        }
    }
    ReviewsScreenContent(
        state = state.value, interactionListener = viewModel
    )
}

@Composable
fun ReviewsScreenContent(
    state: ReviewScreenUiState,
    interactionListener: ReviewScreenInteractionListener,
) {
    NovixScaffold(
        topBar = {
            TopBar(
                screenTitle = stringResource(id = R.string.reviews), leftContent = {
                    TopBarClickableIcon(
                        icon = painterResource(designR.drawable.icon_back),
                        onClick = interactionListener::onBackClick
                    )
                }
            )
        },
        snackBarHost = {
            NovixAnimatedSnackBarHost(
                data = state.snackBarData,
                onDismiss = interactionListener::onSnackBarDismiss,
                modifier = Modifier.statusBarsPadding()
            )
        },
        modifier = Modifier.statusBarsPadding(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
        ) {
            AnimatedContent(
                state,
                modifier = Modifier.align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                val pagedReviews = state.reviews.collectAsLazyPagingItems()
                val isLoading = pagedReviews.loadState.refresh is LoadState.Loading
                val noInternetConnection = it.noInternetConnection
                val noReviews = pagedReviews.itemCount == 0

                when {
                    isLoading -> {
                        LoadingIndicator()
                    }

                    noInternetConnection -> {
                        NetworkDisconnectionContact(
                            onRetryClick = { interactionListener.onRetryClicked() },
                            modifier = Modifier.fillMaxSize(),
                            useDarkTheme = LocalThemeProvider.current
                        )
                    }

                    noReviews -> {
                        EmptyReviewsContent()
                    }

                    else -> {
                        ReviewsList(pagedReviews)
                    }
                }
            }
        }
    }
}

