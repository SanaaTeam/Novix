package com.sanaa.presentation.screen.login.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.text_field.PasswordTextField
import com.sanaa.designsystem.design_system.component.text_field.TextFieldLabel
import com.sanaa.feature.authentication.presentation.R

@Composable
fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    isVisible: Boolean,
    onToggleVisibility: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextFieldLabel(
            text = stringResource(R.string.password),
        )
        PasswordTextField(
            value = value,
            icon = painterResource(R.drawable.lock_key),
            onValueChange = onValueChange,
            isPasswordVisible = isVisible,
            onVisibilityToggle = onToggleVisibility
        )
    }
}