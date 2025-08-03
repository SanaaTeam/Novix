package com.sanaa.presentation.screen.componants

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.text_field.TextField
import com.sanaa.designsystem.design_system.theme.NovixTheme

@Composable
fun SearchSection(
    text: String,
    onTextChange: (String) -> Unit = {},
) {
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
            .height(48.dp)
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),

        )
}

@PreviewLightDark
@Composable
private fun SearchAppBarPreview() {
    var text by remember { mutableStateOf("") }
    NovixTheme(isSystemInDarkTheme()) {
        SearchSection(
            text = text,
            onTextChange = { text = it }
        )
    }
}