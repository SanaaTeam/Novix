package com.sanaa.presentation.bottomsheets.addBookmark

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.base_bottomsheet.BaseBottomSheet
import com.sanaa.designsystem.design_system.component.button.PrimaryButton
import com.sanaa.designsystem.design_system.component.text_field.TextField
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun AddBookmarkListBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    state: AddEditBookmarkListUiState,
    onTitleChanged: (String) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var textFieldValue by remember(state.listTitle) {
        mutableStateOf(
            TextFieldValue(
                text = state.listTitle,
                selection = TextRange(state.listTitle.length)
            )
        )
    }

    val titleRes = if (state.isEditMode) R.string.edit_list else R.string.add_new_list
    val buttonTextRes = if (state.isEditMode) R.string.save else R.string.add

    BaseBottomSheet(
        isVisible = isVisible,
        onDismiss = onDismiss,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TopBar(
                screenTitle = stringResource(titleRes),
                rightContent = {
                    TopBarClickableIcon(
                        icon = painterResource(id = R.drawable.icon_cancel),
                        onClick = onDismiss
                    )
                }
            )

            Text(
                text = stringResource(R.string.list_title),
                style = Theme.textStyle.body.medium,
                color = Theme.colors.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 25.dp, bottom = 8.dp)
            )

            TextField(
                value = textFieldValue,
                onValueChange = { newValue ->
                    if (textFieldValue.text != newValue.text) {
                        onTitleChanged(newValue.text)
                    }
                    textFieldValue = newValue
                },
                hint = stringResource(R.string.my_favorite_placeholder),
                icon = painterResource(id = R.drawable.ic_bookmark_list),
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(start = 16.dp, end = 16.dp)
            )

            PrimaryButton(
                text = stringResource(buttonTextRes),
                onClick = onSaveClick,
                isEnabled = state.isSaveButtonEnabled,
                isLoading = state.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(48.dp)
            )
        }
    }
}


@Preview(name = "Add Mode - Empty", showBackground = true)
@Composable
private fun AddBookmarkListBottomSheetAddEmptyPreview() {
    NovixTheme(isDarkMode = true) {
        AddBookmarkListBottomSheet(
            isVisible = true,
            onDismiss = {},
            state = AddEditBookmarkListUiState(
                isEditMode = false,
                listTitle = "",
                isSaveButtonEnabled = false
            ),
            onTitleChanged = {},
            onSaveClick = {}
        )
    }
}

@Preview(name = "Add Mode - Active", showBackground = true)
@Composable
private fun AddBookmarkListBottomSheetAddActivePreview() {
    var state by remember {
        mutableStateOf(
            AddEditBookmarkListUiState(
                isEditMode = false,
                listTitle = "My favorite",
                isSaveButtonEnabled = true
            )
        )
    }
    NovixTheme(isDarkMode = true) {
        AddBookmarkListBottomSheet(
            isVisible = true,
            onDismiss = {},
            state = state,
            onTitleChanged = { newTitle ->
                state =
                    state.copy(listTitle = newTitle, isSaveButtonEnabled = newTitle.isNotBlank())
            },
            onSaveClick = {}
        )
    }
}


@Preview(name = "Edit Mode", showBackground = true)
@Composable
private fun AddBookmarkListBottomSheetEditModePreview() {
    var state by remember {
        mutableStateOf(
            AddEditBookmarkListUiState(
                listTitle = "My favorite movies",
                isSaveButtonEnabled = true,
                isLoading = false,
                isEditMode = true
            )
        )
    }

    NovixTheme(isDarkMode = true) {
        AddBookmarkListBottomSheet(
            isVisible = true,
            onDismiss = {},
            state = state,
            onTitleChanged = { newTitle ->
                state = state.copy(
                    listTitle = newTitle,
                    isSaveButtonEnabled = newTitle.isNotBlank()
                )
            },
            onSaveClick = {}
        )
    }
}