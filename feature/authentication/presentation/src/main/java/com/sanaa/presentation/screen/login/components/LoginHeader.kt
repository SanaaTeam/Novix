package com.sanaa.presentation.screen.login.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.authentication.presentation.R


@Composable
fun LoginHeader(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(top=12.dp, start = 16.dp,end=16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.app_icon),
            contentDescription = stringResource(R.string.app_icon),
            modifier = Modifier.size(64.dp)
        )
        Text(
            text = stringResource(R.string.login_to_your_account),
            style = Theme.textStyle.title.medium,
            color = Theme.colors.title,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}