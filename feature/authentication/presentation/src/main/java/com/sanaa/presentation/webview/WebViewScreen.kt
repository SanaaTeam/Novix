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
import com.sanaa.designsystem.design_system.component.top_bar.NovixTopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.feature.authentication.presentation.R
import com.sanaa.presentation.navigation.LocalNavControllerProvider

@Composable
fun WebViewScreen(
    url: String,
    onTargetUrlReached: () -> Unit = {},
) {
    val navController = LocalNavControllerProvider.current

    Column(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxSize()
            .testTag("auth_webview")

    ) {
        NovixTopBar(
            modifier = Modifier.fillMaxWidth(),
            screenTitle = stringResource(R.string.sign_up),
            leftContent = {
                TopBarClickableIcon(
                    icon = painterResource(id = R.drawable.icon_back),
                    onClick = { navController.popBackStack() },
                    contentDescription = "icon_cancel"
                )
            }
        )

        AuthWebView(
            webPageUrl = url,
            modifier = Modifier.fillMaxSize(),
            onBackPressed = {
                navController.popBackStack()
            },
            onTargetUrlReached = onTargetUrlReached
        )
    }
}
