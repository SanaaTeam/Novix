package com.sanaa.designsystem.design_system.component.text_field

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun TextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "",
    icon: Painter? = null,
    isEnable: Boolean = true,
    maxLines: Int = 1,
    singleLine: Boolean = true,
    readOnly: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    var internalValue by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(text = value, selection = TextRange(value.length)))
    }

    LaunchedEffect(value) {
        if (value != internalValue.text) {
            internalValue = internalValue.copy(
                text = value,
                selection = TextRange(value.length)
            )
        }
    }

    TextField(
        value = internalValue,
        onValueChange = {
            internalValue = it
            onValueChange(it.text)
        },
        modifier = modifier,
        hint = hint,
        icon = icon,
        isEnable = isEnable,
        maxLines = maxLines,
        singleLine = singleLine,
        readOnly = readOnly,
        interactionSource = interactionSource
    )
}

@Composable
fun TextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "",
    icon: Painter? = null,
    isEnable: Boolean = true,
    maxLines: Int = 1,
    singleLine: Boolean = true,
    readOnly: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    BasicTextField(
        interactionSource = interactionSource,
        value = value,
        onValueChange = onValueChange,
        maxLines = maxLines,
        singleLine = singleLine,
        readOnly = readOnly,
        isEnable = isEnable,
        modifier = modifier,
    ) { innerTextField, hintColor ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    painter = icon,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(24.dp),
                    tint = hintColor
                )
            }
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                if (value.text.isEmpty()) {
                    Text(
                        text = hint,
                        style = Theme.textStyle.body.small,
                        color = Theme.colors.hint
                    )
                }
                innerTextField()
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewNovixTextField() {
    NovixTheme(isSystemInDarkTheme()) {
        var text by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .background(color = Theme.colors.surface)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextFieldLabel(text = "Username")
            TextField(
                value = text,
                onValueChange = { text = it },
                icon = painterResource(R.drawable.icon_user)
            )
            TextField(
                value = text,
                onValueChange = { text = it },
                hint = "Password"
            )
        }
    }
}