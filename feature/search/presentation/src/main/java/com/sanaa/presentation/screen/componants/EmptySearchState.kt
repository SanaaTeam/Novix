package com.sanaa.presentation.screen.componants

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme


@Composable
fun EmptySearchContent(
    icon: Painter,
    text: String,
    modifier: Modifier = Modifier,
    topIcon: @Composable () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .padding(bottom = 12.dp)
                .size(128.dp),

            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = icon,
                contentDescription = "Empty Search",
                modifier = Modifier
                    .size(128.dp)
            )
            topIcon()
        }
        BasicText(
            text = text,
            style = Theme.textStyle.body.small.copy(
                color = Theme.colors.body,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .padding(horizontal = 48.dp)
                .align(Alignment.CenterHorizontally)
        )

    }
}

@PreviewLightDark
@Composable
fun EmptySearchStatePreview() {
    NovixTheme(isSystemInDarkTheme()) {
        EmptySearchContent(
            icon = painterResource(id = R.drawable.empty_search),
            text = stringResource(id = R.string.empty_search_message)
        )
    }
}
