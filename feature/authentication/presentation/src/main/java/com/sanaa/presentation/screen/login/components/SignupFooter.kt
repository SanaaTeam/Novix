package com.sanaa.presentation.screen.login.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.authentication.presentation.R

@Composable
fun SignupFooter(onCreateAccount: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 22.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.don_t_have_an_account),
            style = Theme.textStyle.body.small,
            color = Theme.colors.body
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = stringResource(R.string.create_account),
            style = Theme.textStyle.label.medium,
            color = Theme.colors.primary,
            modifier = Modifier.clickable(onClick = onCreateAccount)
        )
    }
}