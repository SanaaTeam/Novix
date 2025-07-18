package com.sanaa.presentation.screen.review.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.sanaa.designsystem.design_system.component.button.TextButton
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.presentation.R
import com.sanaa.presentation.component.IconWithText
import com.sanaa.presentation.model.ReviewUiModel

@Composable
fun ReviewCard(
    review: ReviewUiModel,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Theme.colors.surface, shape = RoundedCornerShape(12.dp))
            .border(1.dp, Theme.colors.stroke, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, Theme.colors.stroke, RoundedCornerShape(8.dp)),

                contentAlignment = Alignment.Center
            ) {
                if (review.avatarUrl != null) {
                    AsyncImage(
                        model = review.avatarUrl,
                        contentDescription = review.authorName,
                        fallback = painterResource(R.drawable.user_avater),
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        painter = painterResource(R.drawable.user_avater),
                        contentDescription = stringResource(R.string.anonymous),
                        modifier = Modifier
                            .size(28.dp),
                        tint = Theme.colors.hint

                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {

                Text(
                    text = review.authorName.takeUnless { it.isNullOrBlank() }
                        ?: stringResource(R.string.anonymous),
                    style = Theme.textStyle.title.medium,
                    color = Theme.colors.title
                )
                review.username?.let {
                    Text(
                        text = it, style = Theme.textStyle.label.small, color = Theme.colors.hint
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            review.rating?.let {
                IconWithText(
                    text = review.rating,
                    iconRes = R.drawable.icon_star,
                    contentDescription = review.rating,
                    tint = Theme.colors.statusColors.yellowAccent,
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = review.content,
            style = Theme.textStyle.body.small,
            color = Theme.colors.body,
            maxLines = if (expanded) Int.MAX_VALUE else 4,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            )
        )
        if (review.content.length > 150) {
            TextButton(
                onClick = { expanded = !expanded },
                text = if (expanded) stringResource(R.string.read_less) else stringResource(R.string.read_more),
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        IconWithText(
            text = review.createdDate,
            iconRes = R.drawable.icon_calender,
            contentDescription = review.createdDate,
            tint = Theme.colors.hint
        )
    }

}