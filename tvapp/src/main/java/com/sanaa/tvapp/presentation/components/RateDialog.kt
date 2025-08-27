package com.sanaa.tvapp.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
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
    onDeleteRating: () -> Unit = {},
    onRatingChanged: (Int) -> Unit = {},
    isSubmitButtonEnabled: Boolean = true,
    isDeleteButtonVisible: Boolean = true,
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

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {

            Button(
                scale = ButtonDefaults.scale(focusedScale = 1.03f),
                shape = ButtonDefaults.shape(RoundedCornerShape(12.dp)),
                onClick = onSubmitRating,
                enabled = isSubmitButtonEnabled,
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

            if (isDeleteButtonVisible) {
                Button(
                    scale = ButtonDefaults.scale(focusedScale = 1.03f),
                    shape = ButtonDefaults.shape(RoundedCornerShape(12.dp)),
                    onClick = onDeleteRating,
                    colors = ButtonDefaults.colors(
                        contentColor = Theme.colors.statusColors.redAccent,
                        focusedContentColor = Theme.colors.onPrimary,
                        containerColor = Theme.colors.iconBackgroundLow,
                        focusedContainerColor = Theme.colors.statusColors.redAccent
                    ),
                ) {
                    Text(
                        text = stringResource(R.string.delete),
                        style = Theme.textStyle.label.large,
                        color = Theme.colors.onPrimary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview(device = Devices.TV_720p)
@Composable
private fun Preview() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        RateDialog()
    }
}

