package com.sanaa.presentation.screen.myAccount.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.button.OutlinedButton
import com.sanaa.designsystem.design_system.component.text.AppText
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.userprofile.presentation.R
import com.sanaa.presentation.profileProvider.LocalThemeMode

@Composable
fun NotLoggedInStateComponent(
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val placeholderResId = if (LocalThemeMode.current)
        R.drawable.users_placeholder
    else
        R.drawable.users_placeholder_light
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 48.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(placeholderResId),
                contentDescription = null,
                modifier = Modifier.size(128.dp)
            )
            AppText(
                text = stringResource(R.string.please_login_to_access_your_account_details_and_other_features),
                style = Theme.textStyle.body.small,
                color = Theme.colors.body,
                modifier = Modifier.padding(bottom = 12.dp),
                textAlign = TextAlign.Center
            )
            OutlinedButton(
                text = stringResource(R.string.login),
                onClick = onLoginClick,
                modifier = Modifier
            )
        }
    }
}