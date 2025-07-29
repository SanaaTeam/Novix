package com.sanaa.presentation.screen.login.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.button.NovixPrimaryButton
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.authentication.presentation.R

@Composable
fun LoginActions(
    onLogin: () -> Unit,
    onForgot: () -> Unit,
    isEnabled: Boolean,
    isLoading: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        NovixPrimaryButton(
            text = stringResource(R.string.login),
            onClick = onLogin,
            isEnabled = isEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            isLoading = isLoading
        )
        Text(
            text = stringResource(R.string.forgot_password),
            style = Theme.textStyle.label.medium,
            color = Theme.colors.primary,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 12.dp)
                .clickable(onClick = onForgot)
        )
    }
}