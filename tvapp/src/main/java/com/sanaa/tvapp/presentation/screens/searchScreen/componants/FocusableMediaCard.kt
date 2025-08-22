package com.sanaa.tvapp.presentation.screens.searchScreen.componants

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Border
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.Text
import com.sanaa.designsystem.design_system.component.blur.OnBlurContent
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.image_viewer.component.RemoteBlurredSensitiveImage
import com.sanaa.tvapp.R
import com.sanaa.tvapp.presentation.api.LocalSafeContentThreshold


@Composable
fun FocusableMediaCard(
    imageUrl: String,
    titleText: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    topCornerContent: @Composable (() -> Unit)? = null,
) {
    val width: Dp = 153.dp
    val height: Dp = 231.dp
    var isFocused by remember { mutableStateOf(false) }
    val textTopPadding by animateDpAsState(
        targetValue = if (isFocused) 16.dp else 8.dp,
        label = "animatedWidth"
    )

    val textBottomPadding by animateDpAsState(
        targetValue = if (isFocused) 8.dp else 16.dp,
        label = "animatedWidth"
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState -> isFocused = focusState.isFocused },
            onClick = onClick,
            colors = CardDefaults.colors(containerColor = Theme.colors.surfaceHigh),
            border = CardDefaults.border(
                border = Border.None,
                focusedBorder = Border(
                    border = BorderStroke(
                        width = 3.dp,
                        color = Theme.colors.primary,
                    ),
                    shape = RoundedCornerShape(12.dp),
                )
            ),
            shape = CardDefaults.shape(RoundedCornerShape(12.dp))
        ) {
            Box(contentAlignment = Alignment.TopEnd) {
                RemoteBlurredSensitiveImage(
                    isBlurEnabled = LocalSafeContentThreshold.current != 0f,
                    imageUrl = imageUrl,
                    modifier = Modifier
                        .width(width)
                        .height(height),
                    sensitiveContentThreshold = 0.2f,
                    safeContentThreshold = 0.7f,
                    placeholderContent = {
                        RemoteImagePlaceholder(
                            modifier = Modifier
                                .width(width)
                                .height(height)
                        )
                    },
                    errorContent = {
                        RemoteImagePlaceholder(
                            modifier = Modifier
                                .width(width)
                                .height(height)
                        )
                    },
                    contentDescription = titleText,
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    contentAlignment = Alignment.TopEnd
                ) {
                    topCornerContent?.invoke()
                }
            }
        }
        Text(
            text = titleText,
            style = Theme.textStyle.label.medium,
            color = Theme.colors.title,
            modifier = Modifier
                .width(width)
                .padding(top = textTopPadding, bottom = textBottomPadding),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start,
        )
    }
}
