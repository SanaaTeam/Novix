package com.sanaa.tvapp.presentation.screens.myAccount.component


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.Text
import com.sanaa.designsystem.design_system.component.text.AppText
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.tvapp.R
import com.sanaa.tvapp.R as tvRes

@Composable
fun NotLoggedInStateComponent(
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val placeholderResId = tvRes.drawable.users_placeholder
    Column(
        modifier = modifier.padding(horizontal = 48.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(placeholderResId),
            contentDescription = null,
            modifier = Modifier
                .size(80.dp)
                .padding(bottom = 8.dp)
        )
        AppText(
            text = stringResource(tvRes.string.please_login_to_access_your_account_details_and_other_features),
            style = Theme.textStyle.body.small,
            color = Theme.colors.body,
            modifier = Modifier.padding(bottom = 12.dp),
            textAlign = TextAlign.Center
        )

        Button(
            modifier = Modifier,
            onClick = { onLoginClick() },
            scale = ButtonDefaults.scale(focusedScale = 1.03f),
            colors = ButtonDefaults.colors(
                containerColor = Theme.colors.iconBackgroundLow,
                focusedContainerColor = Theme.colors.primary
            ),
            shape = ButtonDefaults.shape(RoundedCornerShape(12.dp)),
        ) {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = stringResource(R.string.login),
                    style = Theme.textStyle.label.large,
                    color = Theme.colors.onPrimary,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}