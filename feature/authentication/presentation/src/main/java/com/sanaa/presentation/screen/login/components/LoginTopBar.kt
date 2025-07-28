package com.sanaa.presentation.screen.login.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
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
        modifier = Modifier
            .systemBarsPadding()
            .padding(vertical = 8.dp)
            .zIndex(10f)
    )
}