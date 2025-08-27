package com.sanaa.tvapp.presentation.screens.login.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.tv.material3.IconButton
import androidx.tv.material3.IconButtonDefaults
import com.sanaa.designsystem.design_system.theme.Theme
import androidx.compose.material3.Icon
import com.sanaa.designsystem.R as designSystemRes

@Composable
fun PasswordInput(
    password: String,
    onValueChange: (String) -> Unit,
    passwordVisible: Boolean,
    onVisibilityToggle: () -> Unit,
) {
    val editTextInteractionSource = remember { MutableInteractionSource() }
    val isEditTextFocused by editTextInteractionSource.collectIsFocusedAsState()

    val eyeInteractionSource = remember { MutableInteractionSource() }
    val isEyeFocused by eyeInteractionSource.collectIsFocusedAsState()

    Row(
        modifier = Modifier
            .background(Theme.colors.surface, RoundedCornerShape(12.dp))
            .fillMaxWidth()
            .border(
                width = if (isEditTextFocused || isEyeFocused) 1.dp else 1.dp,
                color = if (isEditTextFocused || isEyeFocused) Theme.colors.primary else Theme.colors.stroke,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(start = 12.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = designSystemRes.drawable.lock_key),
            contentDescription = null,
            modifier = Modifier
                .padding(end = 8.dp)
                .size(24.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onVisibilityToggle
                ),
            tint = if (isEditTextFocused || isEyeFocused) Theme.colors.primary else Theme.colors.hint,
        )

        BasicTextField(
            value = password,
            onValueChange = { onValueChange(it) },
            singleLine = true,
            visualTransformation = if (passwordVisible)
                VisualTransformation.None
            else
                PasswordVisualTransformation(mask = '*'),
            textStyle = Theme.textStyle.body.medium.copy(color = Theme.colors.body),
            modifier = Modifier.weight(1f),
            interactionSource = editTextInteractionSource,
        )

        IconButton(
            onClick = { onVisibilityToggle() },
            colors = IconButtonDefaults.colors(
                containerColor = Color.Transparent,
                focusedContainerColor = Theme.colors.stroke,
            ),
            shape = IconButtonDefaults.shape(RoundedCornerShape(12.dp)),
            scale = IconButtonDefaults.scale(focusedScale = 1f),
            interactionSource = eyeInteractionSource
        ) {
            Crossfade(
                targetState = passwordVisible,
                animationSpec = tween(50)
            ) { visible ->
                val iconRes = if (visible) {
                    designSystemRes.drawable.icon_eye_without_slash
                } else {
                    designSystemRes.drawable.icon_eye_with_slash
                }

                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = if (isEyeFocused) Theme.colors.primary else Theme.colors.hint,
                )
            }
        }
    }
}