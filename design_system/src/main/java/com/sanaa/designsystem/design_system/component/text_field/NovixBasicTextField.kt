package com.sanaa.designsystem.design_system.component.text_field

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
internal fun NovixBasicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isEnable: Boolean = true,
    maxLines: Int = 1,
    singleLine: Boolean = true,
    readOnly: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable (innerTextField: @Composable () -> Unit, hintColor: Color) -> Unit,
) {
    val isFocused by interactionSource.collectIsFocusedAsState()
    val animatedBorderColor by animateColorAsState(
        targetValue = if (isFocused) Theme.colors.primary else Theme.colors.stroke,
    )
    val animatedHintColor by animateColorAsState(
        targetValue = if (isFocused) Theme.colors.primary else Theme.colors.hint,
    )

    BasicTextField(
        interactionSource = interactionSource,
        value = value,
        onValueChange = onValueChange,
        maxLines = maxLines,
        singleLine = singleLine,
        readOnly = readOnly,
        enabled = isEnable,
        visualTransformation = visualTransformation,
        textStyle = Theme.textStyle.body.medium.copy(color = Theme.colors.body),
        cursorBrush = SolidColor(Theme.colors.primary),
        modifier = modifier
            .height(48.dp)
            .background(
                color = Theme.colors.surface,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.dp,
                color = animatedBorderColor,
                shape = RoundedCornerShape(12.dp)
            ),
        decorationBox = { innerTextField ->
            content(innerTextField, animatedHintColor)
        }
    )
}