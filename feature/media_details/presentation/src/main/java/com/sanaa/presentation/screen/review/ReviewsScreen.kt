package com.sanaa.presentation.screen.review

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sanaa.designsystem.design_system.component.loading.NovixLoadingIndicator
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.top_bar.AppTopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.presentation.R
import com.sanaa.presentation.model.MediaTypeUiModel
import com.sanaa.presentation.navigation.LocalNavControllerProvider
import com.sanaa.presentation.screen.review.components.EmptyReviewsContent
import com.sanaa.presentation.screen.review.components.ReviewCard
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf


@Composable
fun ReviewsScreen(
    seriesId: Int,
    mediaType: String?,
    viewModel: ReviewViewModel = koinViewModel(parameters = {
        parametersOf(seriesId, MediaTypeUiModel.valueOf(mediaType.orEmpty()))
    })
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
    state: ReviewScreenUiState, interactionListener: ReviewScreenInteractionListener
) {
    NovixScaffold(
        topBar = {
            AppTopBar(
                screenTitle = stringResource(id = R.string.reviews), leftContent = {
                    TopBarClickableIcon(
                        icon = painterResource(R.drawable.icon_back),
                        onClick = interactionListener::onBackClick
                    )
                })
        },
        modifier = Modifier.statusBarsPadding(),
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            AnimatedContent(
                state.isLoading,
                modifier = Modifier.align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                if (it) {
                    NovixLoadingIndicator()
                } else {
                    if (state.reviews.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier
                                .padding(horizontal = 16.dp),
                            contentPadding = PaddingValues(vertical = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            items(state.reviews.size, key = { it }) { review ->
                                ReviewCard(review = state.reviews[review])
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            EmptyReviewsContent()
                        }
                    }
                }
            }
        }
    }
}

