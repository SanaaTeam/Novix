package com.sanaa.designsystem.design_system.component.selection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun <T> SelectionComponent(
    options: List<Option<T>>,
    selectedValue: T,
    onOptionSelected: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        options.forEach { option ->
            OptionCard(
                label = option.label,
                onClick = { onOptionSelected(option.value) },
                isSelected = option.value == selectedValue,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )
        }
    }
}

data class Option<T>(
    val label: String,
    val value: T
)

@PreviewLightDark
@Composable
private fun PreviewOptionsCard() {
    var selected by remember { mutableStateOf("Option 1") }
    NovixTheme(false) {
        Column(
            modifier = Modifier
                .background(color = Theme.colors.surface)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SelectionComponent(
                options =
                    listOf(
                        Option("Option 1", "Option 1"),
                        Option("Option 2", "Option 2"),
                        Option("Option 3", "Option 3"),
                        Option("Option 4", "Option 4"),
                    ),
                selectedValue = selected,
                onOptionSelected = { selected = it }
            )
        }
    }
}

