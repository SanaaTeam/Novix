package com.sanaa.presentation.screen.login.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.sanaa.designsystem.design_system.component.text_field.NovixTextField
import com.sanaa.designsystem.design_system.component.text_field.NovixTextFieldLabel
import com.sanaa.feature.authentication.presentation.R

@Composable
fun UsernameField(
    value: String,
    onValueChange: (String) -> Unit
) {
    NovixTextFieldLabel(
        text = stringResource(R.string.user_name),
    )
    NovixTextField(
        value = value,
        onValueChange = onValueChange,
        icon = painterResource(R.drawable.user_name)
    )
}