package com.sanaa.presentation.bottomsheet.saveToListBottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.base_bottomsheet.BaseBottomSheet
import com.sanaa.designsystem.design_system.component.button.OutlinedButton
import com.sanaa.designsystem.design_system.component.button.PrimaryButton
import com.sanaa.designsystem.design_system.component.indicator.WavyProgressIndicator
import com.sanaa.designsystem.design_system.component.text.AppText
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.presentation.components.NovixAnimatedSnackBarHost
import com.sanaa.presentation.components.SnackData
import kotlinx.coroutines.flow.collectLatest
import com.sanaa.feature.home.presentation.R as homeRes

@Composable
fun SaveToListBottomSheet(
    isVisible: Boolean,
    mediaId: Long,
    onDismiss: () -> Unit,
    onCreateNewListClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SaveToListViewModel = hiltViewModel()
) {
    viewModel.getMediaId(mediaId)

    val state by viewModel.state.collectAsState()
    var snack by remember { mutableStateOf<SnackData?>(null) }
    val successMessage =
        stringResource(homeRes.string.added_to_list_successfully)
    val failMessage =
        stringResource(homeRes.string.added_to_list_failed)




    NovixAnimatedSnackBarHost(
        data = snack, onDismiss = { snack = null }
    )


    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                SaveToListEffect.AddedSuccessfully -> {
                    onDismiss()
                    snack = SnackData(
                        message = successMessage,
                        isError = false
                    )
                }

                SaveToListEffect.FailedToAdd -> {
                    snack = SnackData(
                        message = failMessage,
                        isError = true
                    )
                }
            }
        }
    }

    SaveToListBottomSheetContent(
        isVisible = isVisible,
        state = state,
        onDismiss = onDismiss,
        onPlaylistClicked = viewModel::onPlayListClicked,
        onAddClick = {
            viewModel.onAddClicked(
                mediaId = mediaId,
            )
        },
        onCreateNewListClick = onCreateNewListClick,
        modifier = modifier
    )
}

@Composable
private fun SaveToListBottomSheetContent(
    isVisible: Boolean,
    state: SaveToListUiState,
    onDismiss: () -> Unit,
    onPlaylistClicked: (Long) -> Unit,
    onAddClick: () -> Unit,
    onCreateNewListClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
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
                screenTitle = stringResource(R.string.save_to_list),
                rightContent = {
                    TopBarClickableIcon(
                        icon = painterResource(id = R.drawable.icon_cancel),
                        onClick = onDismiss
                    )
                }
            )
            if (state.isLoading && state.playlists.isEmpty()) {
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
                        .heightIn(max = 400.dp)

                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp, bottom = 24.dp),

                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.playlists, key = { it.id }) { playlist ->
                        PlaylistItem(
                            title = playlist.title,
                            itemCount = playlist.itemCount,
                            isSelected = state.selectedListsIds.contains(playlist.id) || playlist.containsMediaItem ,
                            onClick = { onPlaylistClicked(playlist.id) }
                        )
                    }
                }
            }

            PrimaryButton(
                text = stringResource(R.string.add),
                onClick = onAddClick,
                isEnabled = state.isAddButtonEnabled && !state.isLoading,
                isLoading = state.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(48.dp)
            )

            OutlinedButton(
                text = stringResource(R.string.create_new_list),
                onClick = onCreateNewListClick,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }
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


@Preview(showBackground = true)
@Composable
private fun SaveToListBottomSheetPreview() {
    val playlists = listOf(
        PlaylistUiItem(
            id = 1, title = "My favorite", itemCount = 12,
            itemsIds = listOf(1L,2L),
            containsMediaItem = true,
        ),
        PlaylistUiItem(id = 2, title = "My movies", itemCount = 5,
            itemsIds = listOf(1L,2L),
            containsMediaItem = false
        ),
        PlaylistUiItem(id = 3, title = "Watch Later", itemCount = 23,
            itemsIds = listOf(1L,2L),
            containsMediaItem = false
            )
    )

    var state by remember {
        mutableStateOf(
            SaveToListUiState(
                playlists = playlists,
                selectedListsIds = listOf(1L).toMutableList(),
                isAddButtonEnabled = true
            )
        )
    }

    NovixTheme(isDarkMode = true) {
        SaveToListBottomSheetContent(
            isVisible = true,
            state = state,
            onDismiss = {},
            onPlaylistClicked = { selectedId ->

            },
            onAddClick = {
                state = state.copy(isLoading = true)
            },
            onCreateNewListClick = {}
        )
    }
}