package com.sanaa.presentation.screen.review

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.top_bar.AppTopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.presentation.screen.review.components.EmptyReviewsContent
import com.sanaa.presentation.screen.review.components.ReviewCard


@Composable
fun ReviewsScreen(
    viewModel: ReviewViewModel,
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    ReviewsScreenContent(
        state = state.value,
        interactionListener = viewModel
    )
}

@Composable
fun ReviewsScreenContent(
    state: ReviewScreenUiState,
    interactionListener: ReviewScreenInteractionListener
) {
    NovixScaffold(
        topBar = {
            AppTopBar(
                screenTitle = stringResource(id = R.string.reviews),
                leftContent = {
                    TopBarClickableIcon(
                        icon = painterResource(R.drawable.arrow_left),
                        onClick = interactionListener::onBackClick
                    )
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp)
        ) {

            if (state.reviews.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
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

