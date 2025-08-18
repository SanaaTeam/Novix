package com.sanaa.presentation.screen.playlistDetails.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.text.AppText
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.playlists.presentation.R

@Composable
fun EmptyItemsScreen(
    messageText: String,
) {
    NovixScaffold {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
        ) {
            Image(
                painter = painterResource(R.drawable.no_items),
                contentDescription = null,
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .size(128.dp),
                contentScale = ContentScale.Fit
            )
            AppText(
                text = messageText,
                style = Theme.textStyle.body.small,
                color = Theme.colors.body,
                textAlign = TextAlign.Center
            )
        }
    }
}


@PreviewLightDark
@Composable
private fun EmptyListScreenLightPreview() {
    val isDarkTheme = isSystemInDarkTheme()
    NovixTheme(
        isDarkMode = isDarkTheme
    ) {

        EmptyItemsScreen(
            messageText = stringResource(R.string.the_list_is_empty),
        )
    }
}

@PreviewLightDark
@Composable
private fun EmptyListScreenDarkPreview() {
    val isDarkTheme = isSystemInDarkTheme()
    NovixTheme(
        isDarkMode = isDarkTheme
    ) {

        EmptyItemsScreen(
            messageText = stringResource(R.string.the_list_is_empty),
        )
    }

}