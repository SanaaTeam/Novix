package com.sanaa.tvapp.presentation.screens.searchScreen.componants

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.text_field.TextField
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.tvapp.R


@Composable
fun SearchTextField(
    text: String,
    onTextChange: (String) -> Unit = {},
) {
    var isFocused by remember { mutableStateOf(false) }

    val borderColor by animateColorAsState(
        targetValue = if (isFocused) Theme.colors.primary else Color.Transparent
    )

    var textFieldValue by remember { mutableStateOf(TextFieldValue(text = text)) }

    if (textFieldValue.text != text) {
        textFieldValue = TextFieldValue(
            text = text,
            selection = TextRange(text.length)
        )
    }
    TextField(
        value = textFieldValue,
        onValueChange = { newValue ->
            textFieldValue = newValue
            onTextChange(newValue.text)
        },
        hint = stringResource(R.string.search_hint),
        icon = painterResource(R.drawable.icon_search),
        modifier = Modifier
            .height(42.dp)
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            }
            .focusable()
            .padding(horizontal = 36.dp)
            .fillMaxWidth()
            .border(
                width = 3.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
    )
}

@Preview(device = Devices.TV_1080p)
@Composable
private fun SearchTextFieldPrev() {
    var text by remember { mutableStateOf("") }
    NovixTheme(isDarkMode = true) {
        SearchTextField(
            text = text,
            onTextChange = { text = it }
        )
    }
}