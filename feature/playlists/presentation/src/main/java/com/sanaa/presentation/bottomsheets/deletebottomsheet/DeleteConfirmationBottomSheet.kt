package com.sanaa.presentation.bottomsheets.deletebottomsheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.base_bottomsheet.BaseBottomSheet
import com.sanaa.designsystem.design_system.component.button.PrimaryButton
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DeleteConfirmationBottomSheet(
    isVisible: Boolean,
    listId: Long?,
    onDismiss: () -> Unit,
    viewModel: DeleteListViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest {
            onDismiss()
        }
    }

    DeleteConfirmationBottomSheetContent(
        isVisible = isVisible,
        isLoading = state.isLoading,
        onDismiss = onDismiss,
        onDeleteClick = {
            listId?.let { id ->
                viewModel.onDeleteConfirmed(listId = id)
            }
        }
    )
}

@Composable
private fun DeleteConfirmationBottomSheetContent(
    isVisible: Boolean,
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BaseBottomSheet(
        isVisible = isVisible,
        onDismiss = onDismiss,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp, start = 16.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TopBar(
                screenTitle = stringResource(R.string.delete_list),
                rightContent = {
                    TopBarClickableIcon(
                        icon = painterResource(id = R.drawable.icon_cancel),
                        onClick = onDismiss
                    )
                }
            )

            Spacer(Modifier.height(25.dp))

            Image(
                painter = painterResource(id = R.drawable.ic_delete_confirmation),
                contentDescription = null,
                modifier = Modifier.size(100.dp),
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.delete_list_confirmation_message),
                style = Theme.textStyle.body.medium,
                color = Theme.colors.body,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(24.dp))

            PrimaryButton(
                text = stringResource(R.string.delete),
                onClick = onDeleteClick,
                isEnabled = !isLoading,
                isLoading = isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun DeleteConfirmationBottomSheetPreview() {
    NovixTheme(isDarkMode = true) {
        DeleteConfirmationBottomSheetContent(
            isVisible = true,
            isLoading = false,
            onDismiss = {},
            onDeleteClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DeleteConfirmationBottomSheetLoadingPreview() {
    NovixTheme(isDarkMode = true) {
        DeleteConfirmationBottomSheetContent(
            isVisible = true,
            isLoading = true,
            onDismiss = {},
            onDeleteClick = {}
        )
    }
}