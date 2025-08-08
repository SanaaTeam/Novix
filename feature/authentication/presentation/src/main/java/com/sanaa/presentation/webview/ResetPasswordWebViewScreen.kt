package com.sanaa.presentation.webview

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
import com.sanaa.feature.authentication.presentation.R
import com.sanaa.designsystem.design_system.component.navigation.LocalNavControllerProvider

@Composable
fun ResetPasswordWebViewScreen(
    url: String,
) {
    val navController = LocalNavControllerProvider.current

    Column(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxSize()
            .testTag("auth_webview")
    ) {
        TopBar(
            modifier = Modifier.fillMaxWidth(),
            screenTitle = stringResource(R.string.reset_password),
            leftContent = {
                TopBarClickableIcon(
                    icon = painterResource(id = com.sanaa.designsystem.R.drawable.icon_cancel),
                    contentDescription = "icon_cancel",
                    onClick = { navController.popBackStack() }
                )
            }
        )

        AuthWebView(
            webPageUrl = url,
            modifier = Modifier.fillMaxSize(),
            onBackPressed = {
                navController.popBackStack()
            },
        )
    }
} 