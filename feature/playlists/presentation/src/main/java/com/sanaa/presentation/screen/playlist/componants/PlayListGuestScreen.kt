package com.sanaa.presentation.screen.playlist.componants

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.feature.playlists.presentation.R

@Composable
fun PlayListGuestScreen(
    onLoginClick: () -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()
    NovixTheme(
        isDarkMode = isDarkTheme
    ) {
        val myListImg = if (isDarkTheme) {
            R.drawable.no_login_my_list_dark
        } else {
            R.drawable.no_login_my_list_light
        }
        PlayListEmptyStateScreen(
            screenTitle = stringResource(R.string.my_lists),
            messageText = stringResource(R.string.please_login_to_create_lists_and_add_your_favorite_items_to_it),
            imageRes = myListImg,
            showLoginButton = true,
            onLoginClick = {onLoginClick()}
        )
    }
}

@Preview
@Composable
private fun PlayListGuestScreenPrev() {
    PlayListGuestScreen(onLoginClick = {})
}