package com.sanaa.presentation.bottomsheet.addEditBookmark

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import com.sanaa.presentation.components.NovixAnimatedSnackBarHost
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AddBookmarkListBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: AddBookmarkListViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                AddBookmarkEffect.Dismiss -> {
                    onDismiss()
                }
            }
        }
    }

    AddBookmarkListBottomSheetContent(
        isVisible = isVisible,
        onDismiss = onDismiss,
        state = state,
        interactionListener = viewModel,
        modifier = modifier
    )
}


@Composable
private fun AddBookmarkListBottomSheetContent(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    state: AddBookmarkListUiState,
    interactionListener: AddBookmarkInteractionListener,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding(),
    ) {
        BaseBottomSheet(
            isVisible = isVisible,
            onDismiss = onDismiss,
        ) {
            Column(
                modifier = Modifier
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
                    value = TextFieldValue(
                        text = state.listTitle,
                        selection = TextRange(state.listTitle.length)
                    ),
                    onValueChange = { newValue ->
                        interactionListener.onListTitleChanged(newValue.text)
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
                    onClick = { interactionListener.onAddClicked() },
                    isEnabled = state.isAddButtonEnabled,
                    isLoading = state.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(48.dp)
                )
            }
        }

        NovixAnimatedSnackBarHost(
            data = state.snackBarData,
            onDismiss = interactionListener::onSnackBarDismiss,
        )
    }
}

@Preview(name = "Add Mode - Empty", showBackground = true)
@Composable
private fun AddBookmarkListBottomSheetEmptyPreview() {
    NovixTheme(isDarkMode = true) {
        AddBookmarkListBottomSheetContent(
            isVisible = true,
            onDismiss = {},
            state = AddBookmarkListUiState(
                listTitle = "",
                isAddButtonEnabled = false
            ),
            interactionListener = object : AddBookmarkInteractionListener {
                override fun onListTitleChanged(title: String) {}
                override fun resetState() {}
                override fun onAddClicked() {}
                override fun onSnackBarDismiss() {}
            },
        )
    }
}

@Preview(name = "Add Mode - Active", showBackground = true)
@Composable
private fun AddBookmarkListBottomSheetActivePreview() {
    var state by remember {
        mutableStateOf(
            AddBookmarkListUiState(
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
            interactionListener = object : AddBookmarkInteractionListener {
                override fun onListTitleChanged(title: String) {}
                override fun resetState() {}
                override fun onAddClicked() {}
                override fun onSnackBarDismiss() {}
            },
        )
    }
}