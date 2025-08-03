package com.sanaa.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.base_bottomsheet.BaseBottomSheet
import com.sanaa.designsystem.design_system.component.button.PrimaryButton
import com.sanaa.designsystem.design_system.component.selection.Option
import com.sanaa.designsystem.design_system.component.selection.SelectionComponent
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.feature.userprofile.presentation.R

@Composable
fun <T> SelectionBottomSheet(
    bottomSheetTitle: String,
    options: List<Option<T>>,
    onDismiss: () -> Unit,
    onOptionSelected: (T) -> Unit,
    selectedValue: T,
    isVisible: Boolean,
    onSaveClick: () -> Unit,
) {
    BaseBottomSheet(
        onDismiss = onDismiss,
        isVisible = isVisible,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TopBar(
                screenTitle = bottomSheetTitle,
                rightContent = {
                    TopBarClickableIcon(
                        icon = painterResource(id = R.drawable.icon_cancel),
                        onClick = onDismiss
                    )
                }
            )
            SelectionComponent(
                options = options,
                selectedValue = selectedValue,
                onOptionSelected = onOptionSelected,
                modifier = Modifier.padding(
                    vertical = 24.dp, horizontal = 16.dp
                )
            )
            PrimaryButton(
                text = "Save",
                onClick = onSaveClick,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp
                    )
            )
        }
    }
}

@Preview
@Composable
private fun PreviewBottomSheet() {
    NovixTheme(isDarkMode = true) {
        SelectionBottomSheet(
            bottomSheetTitle = "Title",
            options = listOf(
                Option("Option 1", "Option 1", "This a Description"),
                Option("Option 2", "Option 2", "Take a look"),
                Option("Option 3", "Option 3", "Blur"),
                Option("Option 4", "Option 4", "Theme"),
            ),
            onDismiss = {},
            onOptionSelected = {},
            selectedValue = "Option 1",
            isVisible = true,
            onSaveClick = {}
        )
    }
}