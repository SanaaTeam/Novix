package com.sanaa.presentation.screen.myAccount.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.text.AppText
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.presentation.screen.myAccount.UserUiState

@Composable
fun MyAccountUserInfo(user: UserUiState,onLogoutClick:()-> Unit={}) {
    var showLogoutButton by remember { mutableStateOf(false) }
    Box (
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 12.dp,
                    horizontal = 16.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        )
        {
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
                AsyncImage(
                    model = user.imageUrl,
                    contentDescription = null,
                    error = painterResource(id = R.drawable.user_profile),
                    placeholder = painterResource(id = R.drawable.user_profile),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            AppText(
                modifier = Modifier.weight(1f),
                text = user.username.orEmpty(),
                color = Theme.colors.title,
                style = Theme.textStyle.title.medium
            )


            Image(
                modifier = Modifier
                    .height(24.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .clickable {
                        showLogoutButton = !showLogoutButton
                    }
                ,
                painter = painterResource(id = R.drawable.more_vertical),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Theme.colors.body)
            )

        }
        Box(
            modifier = Modifier
                .padding(end = 20.dp, top = 45.dp)
                .size(172.dp, 46.dp)
                .align(Alignment.TopEnd)
        ){
            AnimatedVisibility(
                visible = showLogoutButton,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                LogoutButton {
                    onLogoutClick()
                }
            }
        }
    }

}


@PreviewLightDark
@Composable
fun UserInfoPreview(modifier: Modifier = Modifier) {
    NovixTheme(isDarkMode = isSystemInDarkTheme()) {
        MyAccountUserInfo(UserUiState())
    }
}

@Composable
fun LogoutButton(modifier: Modifier = Modifier,onLogoutClick: () -> Unit={}) {
    Row (
        modifier = modifier
            .fillMaxSize()
            .background(
                color = Theme.colors.surface,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                color = Theme.colors.stroke,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 12.dp)
            .clickable{
                onLogoutClick()
            }
        ,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Image(
            painter = painterResource(R.drawable.icon_logout),
            contentDescription = "logout button"
        )

        AppText(
            modifier = Modifier,
            text = stringResource(com.sanaa.feature.userprofile.presentation.R.string.logout),
            color = Theme.colors.statusColors.redAccent,
            style = Theme.textStyle.title.medium
        )


    }
}

