package com.sanaa.presentation.screen.playlist.componants

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.button.FabButton
import com.sanaa.designsystem.design_system.component.button.OutlinedButton
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.text.AppText
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.playlists.presentation.R
import com.sanaa.presentation.bottomsheets.addEditBookmark.AddBookmarkListBottomSheet

@Composable
fun PlayListEmptyStateScreen(
    @DrawableRes imageRes: Int,
    screenTitle: String,
    messageText: String,
    showFab: Boolean = false,
    showLoginButton: Boolean = false,
    onFabClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    isVisible: Boolean = false,
    onDismissAddBottomSheet: () -> Unit = {}
) {
    NovixScaffold(
        topBar = {
            TopBar(
                screenTitle = screenTitle,
                modifier = Modifier.padding(top = 12.dp)
            )
        },
        floatingActionButton = {
            if (showFab) {
                FabButton(
                    icon = painterResource(id = com.sanaa.designsystem.R.drawable.icon_plus),
                    onClick = onFabClick,
                    isLoading = false,
                    isEnabled = true
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 200.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(imageRes),
                contentDescription = null
            )

            AppText(
                modifier = Modifier
                    .padding(horizontal = 68.dp)
                    .offset(y = (-30).dp),
                text = messageText,
                style = Theme.textStyle.body.small,
                color = Theme.colors.body,
                textAlign = TextAlign.Center
            )

            if (showLoginButton) {
                OutlinedButton(
                    text = stringResource(R.string.login),
                    onClick = onLoginClick
                )
            }
        }
        if (isVisible){
            AddBookmarkListBottomSheet(
                isVisible = isVisible,
                onDismiss = onDismissAddBottomSheet,
                mediaId = 0
            )
        }
    }
}


@PreviewLightDark
@Composable
private fun EmptySavedListUserScreenContentPrev() {
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
            onFabClick = {}
        )
    }
}

@PreviewLightDark
@Composable
private fun EmptySavedListGuestScreenContentPrev() {
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
            onLoginClick = {}
        )
    }

}