package com.sanaa.tvapp.presentation.screens.myAccount.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Border
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.Icon
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.text.AppText
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.tvapp.presentation.screens.myAccount.UserUiState
import com.sanaa.tvapp.R as tvRes

@Composable
fun MyAccountUserInfo(user: UserUiState, onLogoutClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
           .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Theme.colors.iconBackgroundLow)
                .border(
                    1.dp,
                    Theme.colors.stroke,
                    RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(28.dp),
                painter = painterResource(id = R.drawable.user_profile),
                contentDescription = null,
            )
        }

        AppText(
            modifier = Modifier.padding(end = 8.dp),
            text = "@${user.username.orEmpty()}",
            color = Theme.colors.title,
            style = Theme.textStyle.title.medium
        )

        Button(
            shape = ButtonDefaults.shape(RoundedCornerShape(8.dp)),
            colors = ButtonDefaults.colors(containerColor = Theme.colors.surfaceHigh),
            border = ButtonDefaults.border(
                focusedBorder = Border(
                    border = BorderStroke(
                        width = 1.dp,
                        color = Theme.colors.statusColors.redAccent
                    ),
                )
            ),
            onClick = { onLogoutClick() }
        ) {
            Image(
                modifier = Modifier.size(20.dp),
                painter = painterResource(R.drawable.icon_logout),
                contentDescription = "logout button"
            )

            AppText(
                modifier = Modifier.padding(start = 8.dp),
                text = stringResource(tvRes.string.logout),
                color = Theme.colors.statusColors.redAccent,
                style = Theme.textStyle.label.medium
            )
        }
    }

}


@PreviewLightDark
@Composable
fun UserInfoPreview(modifier: Modifier = Modifier) {
    NovixTheme(isDarkMode = isSystemInDarkTheme()) {
        MyAccountUserInfo(UserUiState(username = "AimanYosofi"))
    }
}
