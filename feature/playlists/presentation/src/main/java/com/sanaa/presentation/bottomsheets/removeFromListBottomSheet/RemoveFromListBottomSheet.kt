package com.sanaa.presentation.bottomsheets.removeFromListBottomSheet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.base_bottomsheet.BaseBottomSheet
import com.sanaa.designsystem.design_system.component.button.PrimaryButton
import com.sanaa.designsystem.design_system.component.indicator.WavyProgressIndicator
import com.sanaa.designsystem.design_system.component.text.AppText
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.presentation.screen.playlistDetails.components.AnimatedSnackBarHost
import kotlinx.coroutines.flow.collectLatest


@Composable
fun RemoveFromListBottomSheet(
    isVisible: Boolean,
    mediaId: Int,
    mediaTitle: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RemoveFromListViewModel = hiltViewModel(),
    onDismissAfterRemoveSuccess: (List<Int>) -> Unit
) {

    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is RemoveFromListEffect.DismissBottomSheet -> onDismissAfterRemoveSuccess(effect.deselectedListIds)
            }
        }
    }

    LaunchedEffect(mediaId) {
        viewModel.getMediaId(mediaId)
    }

    RemoveFromListBottomSheetContent(
        isVisible = isVisible,
        mediaTitle = mediaTitle,
        state = state,
        onDismiss = onDismiss,
        interactionListener = viewModel,
        modifier = modifier
    )
}

@Composable
private fun RemoveFromListBottomSheetContent(
    isVisible: Boolean,
    mediaTitle: String,
    state: RemoveFromListUiState,
    onDismiss: () -> Unit,
    interactionListener: RemoveFromListInteractionListener,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding(),
    ) {
        BaseBottomSheet(
            isVisible = isVisible,
            onDismiss = {
                interactionListener.onBottomSheetDismiss()
                onDismiss()
            },
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
            ) {
                TopBar(
                    screenTitle = stringResource(R.string.remove_from_list),
                    rightContent = {
                        TopBarClickableIcon(
                            icon = painterResource(id = R.drawable.icon_cancel),
                            onClick = {
                                interactionListener.onBottomSheetDismiss()
                                onDismiss()
                            }
                        )
                    }
                )
                AppText(
                    text = stringResource(R.string.remove_from_list_caption),
                    style = Theme.textStyle.body.small,
                    color = Theme.colors.body,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )

                AppText(
                    text = mediaTitle,
                    style = Theme.textStyle.body.small,
                    color = Theme.colors.body,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .background(Theme.colors.surfaceHigh, RoundedCornerShape(8.dp))
                        .padding(vertical = 2.dp),
                    textAlign = TextAlign.Center
                )

                if (state.isLoading && state.selectedLists.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .height(200.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        WavyProgressIndicator()
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .heightIn(max = 350.dp)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.selectedLists, key = { it.id }) { playlist ->
                            PlaylistItem(
                                title = playlist.title,
                                itemCount = playlist.itemCount,
                                isSelected = state.deselectedListsIds.contains(playlist.id),
                                onClick = { interactionListener.onPlaylistClick(playlist.id) }
                            )
                        }
                    }
                }

                PrimaryButton(
                    text = stringResource(R.string.remove),
                    onClick = interactionListener::onRemoveClick,
                    isEnabled = state.isRemoveButtonEnabled,
                    isLoading = state.isUploading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(48.dp)
                )
            }
        }

        AnimatedSnackBarHost(
            data = state.snackBarData, onDismiss = interactionListener::onSnackBarDismiss
        )
    }
}

@Composable
private fun PlaylistItem(
    title: String,
    itemCount: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(12.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(
                color = if (isSelected) Theme.colors.primaryVariant
                else Theme.colors.surface
            )
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) Theme.colors.primary
                else Theme.colors.stroke,
                shape = shape
            )
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Column {
            AppText(
                text = title,
                style = Theme.textStyle.body.large,
                color = Theme.colors.title
            )
            Spacer(Modifier.height(4.dp))
            AppText(
                text = "$itemCount items",
                style = Theme.textStyle.body.small,
                color = Theme.colors.body
            )
        }
    }
}
