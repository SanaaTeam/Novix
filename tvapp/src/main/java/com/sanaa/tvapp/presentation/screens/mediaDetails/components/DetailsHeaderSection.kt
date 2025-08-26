package com.sanaa.tvapp.presentation.screens.mediaDetails.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Text
import com.sanaa.designsystem.design_system.component.blur.OnBlurContent
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.image_viewer.component.RemoteBlurredSensitiveImage
import com.sanaa.tvapp.R
import com.sanaa.tvapp.presentation.api.LocalSafeContentThreshold
import com.sanaa.tvapp.presentation.screens.searchScreen.componants.RemoteImagePlaceholder


@Composable
fun DetailsHeaderSection(
    backgroundImageUrl: String,
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize())
    {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(420.dp)
                .align(Alignment.TopEnd)
        ) {
            RemoteBlurredSensitiveImage(
                isBlurEnabled = LocalSafeContentThreshold.current != 0f,
                imageUrl = backgroundImageUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopEnd),
                sensitiveContentThreshold = 0.2f,
                safeContentThreshold = 0.7f,
                placeholderContent = {
                    RemoteImagePlaceholder(Modifier.fillMaxSize())
                },
                errorContent = {
                    RemoteImagePlaceholder(Modifier.fillMaxSize())
                },
                contentDescription = title,
            ) {
                OnBlurContent(
                    hintText = stringResource(R.string.unsuitable_image),
                    textStyle = Theme.textStyle.body.small.copy(
                        color = Color(0x99FFFFFF)
                    ),
                    iconSize = 24.dp,
                    icon = painterResource(com.sanaa.designsystem.R.drawable.icon_eye_slash),
                )
            }
            val brushColor = listOf(
                Color.Transparent,
                Theme.colors.surface.copy(alpha = 0.9f)
            )
            val brushColor2 = listOf(
                Color.Transparent,
                Theme.colors.surface
            )

            Canvas(modifier = Modifier.matchParentSize()) {
                drawRect(
                    brush = Brush.radialGradient(
                        colors = brushColor,
                        center = Offset(size.width - size.width / 4, size.height - 550.dp.toPx()),
                        radius = size.maxDimension - size.minDimension
                    ),
                    size = size
                )
            }

            Canvas(modifier = Modifier.matchParentSize()) {
                drawRect(
                    brush = Brush.verticalGradient(colors = brushColor2),
                    size = size
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 36.dp, vertical = 24.dp)
                .fillMaxWidth(0.5f)
                .align(Alignment.BottomStart),
        ) {
            Text(
                text = title,
                style = Theme.textStyle.title.large,
                color = Theme.colors.title
            )
            content()
        }
    }
}

@Preview(device = Devices.TV_1080p, showBackground = false)
@Composable
private fun HeaderPreview(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .systemBarsPadding()
            .background(Theme.colors.surface)
    ) {
        DetailsHeaderSection(
            backgroundImageUrl = "https://wallpapers.com/movie-poster-background",
            title = "Breaking Bad",
            modifier = modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = "2023 • 5 Seasons • TV-MA",
                    style = Theme.textStyle.body.small,
                    color = Theme.colors.body
                )
                Text(
                    text = "A high school chemistry teacher turned methamphetamine manufacturer faces moral dilemmas and dangerous adversaries.",
                    style = Theme.textStyle.body.small,
                    color = Theme.colors.body
                )
            }
        }
    }
}