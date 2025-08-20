package com.sanaa.presentation.screen.changePassword

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.feature.userprofile.presentation.R
import com.sanaa.presentation.profileProvider.LocalNavControllerProvider

@Composable
fun ChangePasswordWebView(
    url: String,
) {
    val navController = LocalNavControllerProvider.current

    Column(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxSize()
            .testTag("change_password_webview")
    ) {
        TopBar(
            modifier = Modifier.fillMaxWidth(),
            screenTitle = stringResource(R.string.change_password),
            leftContent = {
                TopBarClickableIcon(
                    icon = painterResource(id = com.sanaa.designsystem.R.drawable.icon_cancel),
                    contentDescription = "icon_cancel",
                    onClick = { navController.popBackStack() }
                )
            }
        )

        WebView(
            webPageUrl = url,
            modifier = Modifier.fillMaxSize(),
            onBackPressed = {
                navController.popBackStack()
            },
        )
    }
}