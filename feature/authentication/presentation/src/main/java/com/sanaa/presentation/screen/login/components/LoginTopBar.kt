package com.sanaa.presentation.screen.login.components

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.sanaa.designsystem.design_system.component.top_bar.NovixTopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.feature.authentication.presentation.R

@Composable
fun LoginTopBar(onBackClick: () -> Unit) {
    NovixTopBar(
        leftContent = {
            TopBarClickableIcon(
                icon = painterResource(id = R.drawable.icon_back),
                onClick = onBackClick
            )
        },
        screenTitle = stringResource(R.string.login),
        modifier = Modifier.statusBarsPadding()
    )
}