package com.sanaa.tvapp.presentation.screens.myAccount.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Text
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.designsystem.R as designSystemResource

@Composable
fun SettingSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, Theme.colors.stroke, RoundedCornerShape(8.dp))
            .background(Theme.colors.surfaceHigh)
            .padding(vertical = 12.dp, horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            color = Theme.colors.title,
            style = Theme.textStyle.title.medium
        )

        Row {
            content()
        }
    }
}

data class SettingOptionItem<T>(
    val title: String,
    val description: String = "",
    val tag: T,
    val isSelected: Boolean = false,
)

@Composable
fun <T> SettingOptions(
    settingOptionItems: List<SettingOptionItem<T>>,
    onSelected: (SettingOptionItem<T>) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
    ) {
        settingOptionItems.forEach { item ->
            ToggleableSettingChip(
                settingOptionItem = item,
                onClick = { onSelected(item) }
            )
        }
    }
}

@Composable
fun <T> RowScope.ToggleableSettingChip(
    settingOptionItem: SettingOptionItem<T>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isFocused by remember { mutableStateOf(false) }

    val borderColor by animateColorAsState(
        targetValue = if (isFocused) Theme.colors.primary else Color.Transparent
    )

    val animateBackgroundColor by animateColorAsState(
        targetValue = if (settingOptionItem.isSelected)
            Theme.colors.primary.copy(alpha = 0.12f)
        else Color.Transparent
    )

    Column(
        modifier = modifier
            .weight(1f)
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            }
            .background(
                color = animateBackgroundColor,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
            .padding(8.dp),
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = settingOptionItem.title,
            style = Theme.textStyle.label.medium,
            color = Theme.colors.title,
            textAlign = TextAlign.Center,
        )

        if (settingOptionItem.description.isNotEmpty()) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = settingOptionItem.description,
                style = Theme.textStyle.label.small,
                color = Theme.colors.body,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview(device = "id:tv_720p")
@Composable
private fun SettingSectionPreview() {
    NovixTheme(isDarkMode = true) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .width(1200.dp)
        ) {
            SettingSection(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                title = stringResource(designSystemResource.string.appearance),
            ) {
                SettingOptions(
                    settingOptionItems = listOf(
                        SettingOptionItem(
                            stringResource(designSystemResource.string.system_language),
                            tag = "",
                            isSelected = true
                        ),
                        SettingOptionItem(
                            stringResource(designSystemResource.string.english),
                            tag = "",
                        ),
                        SettingOptionItem(
                            stringResource(designSystemResource.string.arabic),
                            tag = "",
                        ),
                    ),
                    onSelected = {}
                )
            }
        }
    }
}