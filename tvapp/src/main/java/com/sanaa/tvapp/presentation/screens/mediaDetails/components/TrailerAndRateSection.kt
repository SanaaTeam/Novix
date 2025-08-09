package com.sanaa.tvapp.presentation.screens.mediaDetails.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.button.PrimaryButton
import com.sanaa.designsystem.R as designSystemResource
import com.sanaa.tvapp.R


@Composable
fun TrailerAndRateSection(
    modifier: Modifier = Modifier,
    trailerUrl: String? = null,
    onPlayTrailerClicked: () -> Unit = {},
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PrimaryButton(
            text = stringResource(R.string.play_trailer),
            isEnabled = trailerUrl != null,
            onClick = onPlayTrailerClicked,
        )
    }
}


@Preview
@Composable
fun TrailerPreview() {
    TrailerAndRateSection()
}