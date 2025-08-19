package com.sanaa.tvapp.presentation.screens.mediaDetails.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.base_bottomsheet.BaseBottomSheet
import com.sanaa.designsystem.design_system.component.button.PrimaryButton
import com.sanaa.designsystem.design_system.component.text.AppText
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.designsystem.R as designSystemResource
import com.sanaa.tvapp.R

@Composable
fun RateBottomSheet(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    isRateSelected: Boolean = true,
    isUiStateLoading: Boolean = false,
    imdbRating: Int = 0,
    onDismiss: () -> Unit = {},
    onRatingChanged: (Int) -> Unit = {},
    onSubmitButtonClick: () -> Unit = {}
) {

    BaseBottomSheet(
        isVisible = isVisible,
        onDismiss = onDismiss,
        content = {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TopBar(
                    screenTitle = stringResource(R.string.rate_it),
                    rightContent = {
                        TopBarClickableIcon(
                            icon = painterResource(id = designSystemResource.drawable.icon_cancel),
                            onClick = onDismiss
                        )
                    }
                )
                AppText(
                    text = stringResource(R.string.select_how_much_you_like_it),
                    style = Theme.textStyle.body.medium,
                    color = Theme.colors.body,
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 12.dp, start = 16.dp)
                        .align(Alignment.Start)
                )
                AnimatedVisibility(visible = !isUiStateLoading) {
                    ImdbRatingSelector(
                        currentRating = imdbRating,
                        onRatingChanged = onRatingChanged
                    )
                }
                PrimaryButton(
                    isEnabled = isRateSelected,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 24.dp),
                    text = stringResource(R.string.submit),
                    onClick = onSubmitButtonClick
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun RateBottomSheetPreview() {
    val showSheet = remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize()) {

            AppText(text = "Show Bottom Sheet",
                modifier = Modifier.clickable(onClick =
                    { showSheet.value = true }
                )
            )

        if (showSheet.value) {
            RateBottomSheet(isVisible = showSheet.value,
                onDismiss = { showSheet.value = false }
            )
        }
    }
}