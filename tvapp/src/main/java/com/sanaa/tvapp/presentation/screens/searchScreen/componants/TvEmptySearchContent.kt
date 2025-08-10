package com.sanaa.tvapp.presentation.screens.searchScreen.componants

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
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.tvapp.R


@Composable
fun TvEmptySearchContent(
    modifier: Modifier = Modifier,
    icon: Painter = painterResource(id = R.drawable.icon_search_tv),
    text: String = stringResource(id = R.string.empty_search_message),
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
                .size(150.dp),

            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = icon,
                contentDescription = "Empty Search",
                modifier = Modifier
                    .size(150.dp)
            )
            topIcon()
        }
        BasicText(
                text = text,
                style = Theme.textStyle.body.large.copy(
                color = Theme.colors.body,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .padding(horizontal = 48.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Preview(device = Devices.TV_1080p, showBackground = true)
@Composable
fun EmptySearchStatePreview() {
    NovixTheme(isSystemInDarkTheme()) {
        TvEmptySearchContent(
            icon = painterResource(id = R.drawable.icon_search_tv),
            text = stringResource(id = R.string.empty_search_message)
        )
    }
}