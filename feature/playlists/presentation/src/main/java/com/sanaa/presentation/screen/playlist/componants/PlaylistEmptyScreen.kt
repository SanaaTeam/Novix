package com.sanaa.presentation.screen.playlist.componants

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.feature.playlists.presentation.R

@Composable
fun PlaylistEmptyScreen(
    onFabClick: () -> Unit = {},
    isVisible: Boolean = false,
    onDismissAddBottomSheet: () -> Unit = {}
) {
    val isDarkTheme = isSystemInDarkTheme()
    NovixTheme(
        isDarkMode = isDarkTheme
    ) {
        val myListImg = if (isDarkTheme) {
            R.drawable.my_list_dark
        } else {
            R.drawable.my_list_light
        }
        PlayListEmptyStateScreen(
            screenTitle = stringResource(R.string.saved_list),
            messageText = stringResource(R.string.there_is_no_saved_list_yet_click_on_button_to_add_a_new_list),
            imageRes = myListImg,
            showFab = true,
            onFabClick = { onFabClick() },
            isVisible = isVisible,
            onDismissAddBottomSheet = onDismissAddBottomSheet,
        )
    }
}

@Preview
@Composable
private fun PlaylistEmptyScreenPrev() {
    PlaylistEmptyScreen(
    )
}