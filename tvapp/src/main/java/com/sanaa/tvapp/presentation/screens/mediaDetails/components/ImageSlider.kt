package com.sanaa.tvapp.presentation.screens.mediaDetails.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Text
import com.sanaa.designsystem.design_system.component.blur.OnBlurContent
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.image_viewer.component.RemoteBlurredSensitiveImage
import com.sanaa.tvapp.presentation.screens.searchScreen.componants.RemoteImagePlaceholder


@Composable
fun ImagesSlider(
    title: String,
    images: List<String>,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = title,
            style = Theme.textStyle.headLine.small,
            color = Theme.colors.title,
            modifier = Modifier.padding(horizontal = 36.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 36.dp)
        ) {
            itemsIndexed(images) { index, image ->

                ImageCard(image)
            }
        }
    }
}


@Composable
private fun ImageCard(
    imageUrl: String?,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .size(88.dp)
            .border(
                BorderStroke(1.dp, Theme.colors.stroke),
                RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
    ) {

        RemoteBlurredSensitiveImage(
            imageUrl = imageUrl.orEmpty(),
            modifier = Modifier.fillMaxWidth(),
            sensitiveContentThreshold = 0.2f,
            contentDescription = null,
            placeholderContent = {
                RemoteImagePlaceholder(Modifier.fillMaxSize())
            },
            errorContent = {
                RemoteImagePlaceholder(Modifier.fillMaxSize())
            },
        ) {
            OnBlurContent(
                iconSize = 24.dp,
                icon = painterResource(com.sanaa.designsystem.R.drawable.icon_eye_slash),
            )
        }
    }
}