package com.sanaa.presentation.screen.review.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.blur.OnBlurContent
import com.sanaa.designsystem.design_system.component.text.AppText
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.mediadetails.presentation.R
import com.sanaa.image_viewer.component.RemoteBlurredSensitiveImage
import com.sanaa.presentation.api.LocalSafeContentThreshold
import com.sanaa.presentation.model.ReviewUiModel
import com.sanaa.presentation.shared_component.ExpandableText
import com.sanaa.presentation.shared_component.IconWithText
import com.sanaa.designsystem.R as designSystemR

@Composable
fun ReviewCard(
    review: ReviewUiModel,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Theme.colors.surface, shape = RoundedCornerShape(12.dp))
            .border(1.dp, Theme.colors.stroke, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Row {
            UserImage(review)
            UserInfo(review)
            ReviewRate(review)
        }

        ExpandableText(
            modifier = Modifier.padding(top = 12.dp),
            text = review.content,
            style = Theme.textStyle.body.small,
            color = Theme.colors.body,
            collapsedMaxLines = 5,
            readMoreText = " ${stringResource(R.string.read_more)}",
            readLessText = " ${stringResource(R.string.read_less)}",
            onReadMore = { }
        )

        IconWithText(
            modifier = Modifier.padding(top = 12.dp),
            text = review.createdDate,
            iconRes = R.drawable.icon_calender,
            contentDescription = review.createdDate,
            tint = Theme.colors.body
        )
    }
}

@Composable
private fun UserImage(review: ReviewUiModel) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(shape = RoundedCornerShape(12.dp))
            .background(color = Theme.colors.iconBackgroundLow)
            .border(1.dp, Theme.colors.stroke, RoundedCornerShape(12.dp)),

        contentAlignment = Alignment.Center
    ) {
        RemoteBlurredSensitiveImage(
            imageUrl = review.avatarUrl.orEmpty(),
            modifier = Modifier.fillMaxWidth(),
            sensitiveContentThreshold = 0.2f,
            isBlurEnabled = LocalSafeContentThreshold.current != 0f,
            safeContentThreshold = LocalSafeContentThreshold.current,
            contentDescription = review.authorName,
            placeholderContent = {
                Image(
                    painter = painterResource(R.drawable.user_avater),
                    contentDescription = stringResource(R.string.anonymous),
                    modifier = Modifier
                        .size(28.dp)
                        .align(Alignment.Center),
                    colorFilter = ColorFilter.tint(Theme.colors.hint)

                )
            },
            errorContent = {
                Image(
                    painter = painterResource(R.drawable.user_avater),
                    contentDescription = stringResource(R.string.anonymous),
                    modifier = Modifier
                        .size(28.dp)
                        .align(Alignment.Center),
                    colorFilter = ColorFilter.tint(Theme.colors.hint)
                )
            },
        ) {
            OnBlurContent(
                iconSize = 24.dp,
                icon = painterResource(com.sanaa.designsystem.R.drawable.icon_eye_slash),
            )
        }
    }
}

@Composable
private fun RowScope.UserInfo(review: ReviewUiModel) {
    Column(modifier = Modifier
        .weight(1f)
        .padding(start = 8.dp)) {
        AppText(
            text = review.authorName.takeUnless { it.isNullOrBlank() }
                ?: stringResource(R.string.anonymous),
            style = Theme.textStyle.title.medium,
            color = Theme.colors.title
        )
        review.username?.let {
            AppText(
                text = it,
                style = Theme.textStyle.label.small,
                color = Theme.colors.hint
            )
        }
    }
}

@Composable
private fun ReviewRate(review: ReviewUiModel) {
    review.rating?.let {
        IconWithText(
            modifier = Modifier.padding(top = 4.dp),
            text = review.rating,
            textColor = Theme.colors.title,
            iconRes = designSystemR.drawable.icon_star,
            contentDescription = review.rating,
            tint = Theme.colors.statusColors.yellowAccent,
        )
    }
}