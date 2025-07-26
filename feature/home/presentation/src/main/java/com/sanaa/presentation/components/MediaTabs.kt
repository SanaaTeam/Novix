package com.sanaa.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.home.presentation.R
import com.sanaa.presentation.state.MediaType

@Composable
fun MediaTabs(
    onTabClick: (MediaType) -> Unit,
    modifier: Modifier = Modifier,
    selectedTab: MediaType = MediaType.MOVIE,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomCenter
    ) {
        Divider()
        Row(
            modifier = Modifier
                .height(40.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TabButton(
                text = stringResource(R.string.movies),
                onClick = onTabClick,
                isSelected = selectedTab == MediaType.MOVIE,
                mediaType = MediaType.MOVIE,
                modifier = Modifier.weight(1f)
            )
            TabButton(
                text = stringResource(R.string.tvshows),
                onClick = onTabClick,
                isSelected = selectedTab == MediaType.TV_SHOW,
                mediaType = MediaType.TV_SHOW,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun TabButton(
    text: String,
    onClick: (MediaType) -> Unit,
    modifier: Modifier = Modifier,
    mediaType: MediaType,
    isSelected: Boolean = false,
    selectedTextColor: Color = Theme.colors.title,
    notSelectedTextColor: Color = Theme.colors.hint,
) {
    val animatedTextColor by animateColorAsState(
        targetValue = if (isSelected) selectedTextColor else notSelectedTextColor,
    )

    val animatedLineWidthDp by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
    )
    Box(
        modifier = modifier
            .fillMaxHeight()
            .clickable { onClick(mediaType) },
        contentAlignment = Alignment.Center
    ) {
        BasicText(
            text = text,
            style = Theme.textStyle.label.medium.copy(animatedTextColor),
        )
        Box(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .height(3.dp)
                .fillMaxWidth(animatedLineWidthDp)
                .background(Theme.colors.primary, RoundedCornerShape(100.dp))
                .align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun Divider(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(0.5.dp)
            .background(Theme.colors.stroke),
    )
}

@Preview
@Composable
fun MediaTabsPreview() {
    MediaTabs(onTabClick = {})
}