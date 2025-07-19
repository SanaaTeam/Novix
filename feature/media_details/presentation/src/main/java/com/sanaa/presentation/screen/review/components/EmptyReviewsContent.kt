package com.sanaa.presentation.screen.review.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun EmptyReviewsContent(
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.icon_chat),
            contentDescription = "chat",
            modifier = Modifier.size(128.dp)
        )
        Text(
            text = stringResource(id = R.string.no_review),
            style = Theme.textStyle.body.small,
            color = Theme.colors.body
        )
    }
}

@PreviewLightDark
@Composable
fun EmptyReviewsPreview() {
    NovixTheme(isDarkMode = isSystemInDarkTheme()) {
        EmptyReviewsContent(modifier = Modifier.background(Theme.colors.surface))
    }
}