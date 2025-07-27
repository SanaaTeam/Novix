package com.sanaa.designsystem.design_system.component.text_field

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    isPasswordVisible: Boolean,
    onVisibilityToggle: () -> Unit,
    modifier: Modifier = Modifier,
    icon: Painter? = null,
    hint: String = "",
    isEnable: Boolean = true,
    iconDescription: String? = null
) {
    val visualTransformation =
        if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()

    Column(modifier = modifier) {
        NovixBasicTextField(
            value = value,
            onValueChange = onValueChange,
            visualTransformation = visualTransformation,
            isEnable = isEnable,
        ) { innerTextField, hintColor ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                icon?.let {
                    LeadingIcon(painter = it, description = iconDescription, tint = hintColor)
                }

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = hint,
                            style = Theme.textStyle.body.small,
                            color = Theme.colors.hint
                        )
                    }
                    innerTextField()
                }

                Crossfade(
                    targetState = isPasswordVisible,
                    animationSpec = tween(50)
                ) { visible ->
                    val iconRes = if (visible) {
                        R.drawable.icon_eye_without_slash
                    } else {
                        R.drawable.icon_eye_with_slash
                    }
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .size(24.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                enabled = isEnable,
                                onClick = onVisibilityToggle
                            ),
                        tint = hintColor
                    )
                }
            }
        }
    }
}

@Composable
private fun LeadingIcon(
    painter: Painter,
    description: String?,
    tint: androidx.compose.ui.graphics.Color
) {
    Icon(
        painter = painter,
        contentDescription = description,
        modifier = Modifier
            .padding(end = 8.dp)
            .size(24.dp),
        tint = tint
    )
}

@PreviewLightDark
@Composable
private fun PreviewPasswordTextField() {
    NovixTheme(true) {
        var text by remember { mutableStateOf("") }
        var visible by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .background(color = Theme.colors.surface)
                .padding(24.dp)
        ) {
            NovixTextFieldLabel(
                text = "Password",
                modifier = Modifier.padding(bottom = 8.dp)
            )

            PasswordTextField(
                value = text,
                onValueChange = { text = it },
                isPasswordVisible = visible,
                onVisibilityToggle = { visible = !visible },
                hint = "Enter password",
                icon = painterResource(id = R.drawable.lock_key),
                iconDescription = "Password icon"
            )
        }
    }
}