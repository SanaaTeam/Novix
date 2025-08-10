package com.sanaa.tvapp.presentation.screens.searchScreen.componants

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Text
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun TvMediaPosterCard(
    modifier: Modifier = Modifier,
    title: String = "",
    onCardClick: () -> Unit = {},
    topLeftContent: @Composable () -> Unit = {},
    posterImage: @Composable () -> Unit = {}
) {
    var isFocused by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
            .aspectRatio(
                ratio = (158 / 210f)
            )
            .size(width = 231.dp, height = 153.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Theme.colors.surface)
            .focusable()
            .onFocusChanged { isFocused = it.isFocused }
            .clickable(onClick = onCardClick)
    ) {
        posterImage()
        Box(
            modifier = Modifier
                .border(1.dp, Theme.colors.stroke, RoundedCornerShape(12.dp))
                .padding(8.dp)
                .matchParentSize()
                .align(Alignment.TopStart),
        ) {
            topLeftContent()
        }
        if (isFocused) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(top = 10.dp)
            ) {
                Text(
                    text = title,
                    color = Theme.colors.primary,
                    style = Theme.textStyle.label.medium
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewMediaPosterCard() {
    NovixTheme(isSystemInDarkTheme()) {
        Column(modifier = Modifier.fillMaxSize()) {
            TvMediaPosterCard(
                title = "ghsjhvj",
                posterImage = {
                    Image(
                        painter = painterResource(R.drawable.icon_placeholder_light),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            )
        }
    }
}