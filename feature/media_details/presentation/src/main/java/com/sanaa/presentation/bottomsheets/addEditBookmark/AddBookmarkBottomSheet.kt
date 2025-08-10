package com.sanaa.presentation.bottomsheets.addEditBookmark

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.base_bottomsheet.BaseBottomSheet
import com.sanaa.designsystem.design_system.component.button.PrimaryButton
import com.sanaa.designsystem.design_system.component.text_field.TextField
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.presentation.screen.movieDetails.SnackData
import com.sanaa.presentation.shared_component.NovixAnimatedSnackBarHost
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AddBookmarkListBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    mediaId: Int,
) {
    val viewModel: AddBookmarkViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    var snack by remember { mutableStateOf<SnackData?>(null) }
    var successMessage =
        stringResource(com.sanaa.feature.mediadetails.presentation.R.string.created_list_successfully)
    var failMessage =
        stringResource(com.sanaa.feature.mediadetails.presentation.R.string.failed_to_create_list)

    val handleDismiss = {
        viewModel.resetState()
        onDismiss()
    }

    NovixAnimatedSnackBarHost(
        data = snack, onDismiss = { snack = null })

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                AddBookmarkEffects.AddSuccess -> {
                    handleDismiss()
                    snack = SnackData(
                        message = successMessage,
                        isError = false
                    )
                }

                AddBookmarkEffects.AddFailure -> {
                    snack = SnackData(
                        message = failMessage,
                        isError = true
                    )
                }
            }
        }
    }
    AddBookmarkListBottomSheetContent(
        isVisible = isVisible,
        onDismiss = handleDismiss,
        state = state,
        onTitleChanged = viewModel::onListTitleChanged,
        onAddClick = { viewModel.onAddClicked(mediaId) }
    )
}

@Composable
private fun AddBookmarkListBottomSheetContent(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    state: AddBookmarkUiState,
    onTitleChanged: (String) -> Unit,
    onAddClick: () -> Unit,
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
                screenTitle = stringResource(R.string.add_new_list),
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
                text = stringResource(R.string.add),
                onClick = onAddClick,
                isEnabled = state.isAddButtonEnabled,
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
private fun AddBookmarkListBottomSheetEmptyPreview() {
    NovixTheme(isDarkMode = true) {
        AddBookmarkListBottomSheetContent(
            isVisible = true,
            onDismiss = {},
            state = AddBookmarkUiState(
                listTitle = "",
                isAddButtonEnabled = false
            ),
            onTitleChanged = {},
            onAddClick = {}
        )
    }
}

@Preview(name = "Add Mode - Active", showBackground = true)
@Composable
private fun AddBookmarkListBottomSheetActivePreview() {
    var state by remember {
        mutableStateOf(
            AddBookmarkUiState(
                listTitle = "My favorite",
                isAddButtonEnabled = true
            )
        )
    }
    NovixTheme(isDarkMode = true) {
        AddBookmarkListBottomSheetContent(
            isVisible = true,
            onDismiss = {},
            state = state,
            onTitleChanged = { newTitle ->
                state = state.copy(listTitle = newTitle, isAddButtonEnabled = newTitle.isNotBlank())
            },
            onAddClick = {}
        )
    }
}