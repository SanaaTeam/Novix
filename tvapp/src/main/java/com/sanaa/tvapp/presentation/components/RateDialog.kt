package com.sanaa.tvapp.presentation.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.theme.Theme


@Composable
fun RateDialog(
    currentRating: Int = 0,
    onDismissRequest: () -> Unit = {},
    onSubmitRating: () -> Unit = {},
    onRatingChanged: (Int) -> Unit = {},
) {
    DialogBaseComponent(onDismissRequest = onDismissRequest) {
        Text(
            text = stringResource(com.sanaa.tvapp.R.string.rate_it),
            style = Theme.textStyle.title.large,
            color = Theme.colors.title,
            textAlign = TextAlign.Center
        )

        ImdbRatingSelector(
            currentRating = currentRating,
            onRatingChanged = onRatingChanged
        )

        Button(
            scale = ButtonDefaults.scale(focusedScale = 1.03f),
            shape = ButtonDefaults.shape(RoundedCornerShape(12.dp)),
            onClick = { onSubmitRating() },
            colors = ButtonDefaults.colors(
                containerColor = Theme.colors.iconBackgroundLow,
                focusedContainerColor = Theme.colors.primary
            ),
        ) {
            Text(
                text = stringResource(R.string.save),
                style = Theme.textStyle.label.large,
                color = Theme.colors.onPrimary,
                textAlign = TextAlign.Center
            )
        }

    }
}

