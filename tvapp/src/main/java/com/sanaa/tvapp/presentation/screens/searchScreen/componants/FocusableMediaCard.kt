package com.sanaa.tvapp.presentation.screens.searchScreen.componants

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Border
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.Text
import com.sanaa.designsystem.design_system.component.blur.OnBlurContent
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.image_viewer.component.RemoteBlurredSensitiveImage
import com.sanaa.tvapp.R


@Composable
fun FocusableMediaCard(
    imageUrl: String,
    titleText: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    var isFocused by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState -> isFocused = focusState.isFocused },
            onClick = onClick,
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
            RemoteBlurredSensitiveImage(
                imageUrl = imageUrl,
                modifier = Modifier
                    .width(200.dp)
                    .height(300.dp),
                sensitiveContentThreshold = 0.2f,
                safeContentThreshold = 0.7f,
                placeholderContent = {
                    RemoteImagePlaceholder(
                        modifier = Modifier
                            .width(200.dp)
                            .height(300.dp)
                    )
                },
                errorContent = {
                    RemoteImagePlaceholder(
                        modifier = Modifier
                            .width(200.dp)
                            .height(300.dp)
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
        }

        if (isFocused) {
            Text(
                text = titleText,
                style = Theme.textStyle.label.large,
                color = Theme.colors.primary,
                modifier = Modifier
                    .padding(top = 24.dp)
                    .align(Alignment.Start)
                    .width(200.dp),
                maxLines = 1
            )
        }
    }
}