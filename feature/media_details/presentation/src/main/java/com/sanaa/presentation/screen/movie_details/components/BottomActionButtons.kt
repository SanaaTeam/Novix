package com.sanaa.presentation.screen.movie_details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.button.PrimaryButton
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.presentation.component.boxContainerGradient
import com.sanaa.presentation.model.MovieDetailsUiModel
import com.sanaa.designsystem.R as designR
import com.sanaa.presentation.R as presentationR
@Composable
fun BottomActionButtons(
    onWatchTrailer: () -> Unit,
    movieDetails: MovieDetailsUiModel,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .navigationBarsPadding()
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .background(brush = boxContainerGradient)
        )
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PrimaryButton(
                text = null,
                onClick = {},
                modifier = Modifier,
                icon = painterResource(id = designR.drawable.outlined_star),
                iconTint = Theme.colors.onPrimary
            )

            PrimaryButton(
                text = stringResource(id = presentationR.string.play_trailer),
                isEnabled = movieDetails.trailerUrl != null,
                onClick = onWatchTrailer,
                modifier = Modifier
                    .weight(1f)
            )
        }
    }
}