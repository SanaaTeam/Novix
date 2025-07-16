package com.sanaa.presentation.screen.series.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.button.TextButton
import com.sanaa.presentation.R
import com.sanaa.presentation.component.ImageSlider
import com.sanaa.presentation.component.InfoSection
import com.sanaa.presentation.screen.series.SeriesScreenInteractionListener
import com.sanaa.presentation.screen.series.SeriesScreenUiState

@Composable
fun SeriesTopScreen(
    interactionListener: SeriesScreenInteractionListener,
    state: SeriesScreenUiState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        ImageSlider(
            images = state.images,
            modifier = Modifier.align(Alignment.TopCenter),
            contentDescription = null
        )
        InfoSection(
            title = state.series.title,
            modifier = Modifier
                .padding(top = 208.dp, start = 16.dp, end = 16.dp)
                .height(158.dp)
                .align(Alignment.TopCenter)

        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxSize()
            ) {
                TextButton(
                    text = stringResource(R.string.view_reviews),
                    onClick = { interactionListener.onViewReviewsClicked(state.series.id) },
                )
            }

        }
    }
}