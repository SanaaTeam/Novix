package com.sanaa.tvapp.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun TvErrorStateContent(
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
    errorTitle: String,
    errorMessage: String,
    topImage: @Composable () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        topImage()

        Text(
            text = errorTitle,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            color = Theme.colors.title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = errorMessage,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            color = Theme.colors.body,
        )

        Spacer(modifier = Modifier.height(16.dp))


        Button(
            scale = ButtonDefaults.scale(focusedScale = 1.03f),
            shape = ButtonDefaults.shape(RoundedCornerShape(12.dp)),
            onClick = onRetryClick,
            colors = ButtonDefaults.colors(
                containerColor = Theme.colors.iconBackgroundLow,
                focusedContainerColor = Theme.colors.primary
            ),
        ) {
            Text(
                text = stringResource(R.string.offline_note),
                style = Theme.textStyle.label.large,
                color = Theme.colors.onPrimary,
                textAlign = TextAlign.Center
            )
        }
    }
}