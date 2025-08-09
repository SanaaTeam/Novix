package com.sanaa.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.base_bottomsheet.BaseBottomSheet
import com.sanaa.designsystem.design_system.component.button.OutlinedButton
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.home.presentation.R
import com.sanaa.presentation.providers.LocalThemeProvider

@Composable
fun RequestToLoginBottomSheet(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    onDismiss: () -> Unit = {},
    onLoginButtonClick: () -> Unit = {},
    text: String = stringResource(R.string.request_login),
    title: String = stringResource(R.string.add_to_list)

) {
    val isDarkTheme = LocalThemeProvider.current
    val loginImageId = if (isDarkTheme) {
        R.drawable.icon_users_dark
    } else {
        R.drawable.icon_users_light
    }
    BaseBottomSheet(
        isVisible = isVisible,
        onDismiss = onDismiss,
        content = {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TopBar(
                    screenTitle = title,
                    rightContent = {
                        TopBarClickableIcon(
                            icon = painterResource(id = com.sanaa.designsystem.R.drawable.icon_cancel),
                            onClick = onDismiss
                        )
                    }
                )
                Image(
                    painter = painterResource
                        (id = loginImageId),
                    contentDescription = "pleas login light",
                    modifier = Modifier.height(100.dp),
                )
                BasicText(
                    text = text,
                    style = Theme.textStyle.body.small.copy(color = Theme.colors.body),
                    modifier = Modifier.padding(bottom = 24.dp, top = 12.dp)
                )
                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    text = stringResource(R.string.login),
                    onClick = onLoginButtonClick
                )
            }
        }
    )
}