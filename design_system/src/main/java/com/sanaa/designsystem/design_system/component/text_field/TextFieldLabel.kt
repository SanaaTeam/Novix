package com.sanaa.designsystem.design_system.component.text_field

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun TextFieldLabel(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = Theme.textStyle.title.small,
        color = Theme.colors.title,
        modifier = modifier.fillMaxWidth()
    )
}

@PreviewLightDark
@Composable
private fun NovixTextFieldLabelPreview() {
    NovixTheme(
        isDarkMode = isSystemInDarkTheme()
    ) {
        Column(
            modifier = Modifier
                .background(color = Theme.colors.surface)
                .padding(16.dp)
        ) {
            TextFieldLabel(text = "Title")
        }
    }

}