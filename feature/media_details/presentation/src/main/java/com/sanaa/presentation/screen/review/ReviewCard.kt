package com.sanaa.presentation.screen.review

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun ReviewCard(
    review: ReviewUiState,
) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Theme.colors.surface, shape = RoundedCornerShape(12.dp))
            .border(1.dp, Theme.colors.stroke, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = ImageRequest
                    .Builder(context)
                    .data(review.avatarUrl)
                    .build(),
                contentDescription = review.authorName,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, Theme.colors.stroke, RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = review.authorName,
                    style = Theme.textStyle.title.medium,
                    color = Theme.colors.title
                )
                review.username?.let {
                    Text(
                        text = it,
                        style = Theme.textStyle.label.small,
                        color = Theme.colors.hint
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.star),
                    contentDescription = stringResource(id = R.string.rating),
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = review.rating?.toString() ?: "-",
                    style = Theme.textStyle.label.small,
                    color = Theme.colors.title
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = review.content,
            style = Theme.textStyle.body.small,
            color = Theme.colors.body,
            maxLines = if (expanded) Int.MAX_VALUE else 4,
            overflow = TextOverflow.Ellipsis
        )
        if (review.content.length > 150 && !expanded) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(id = R.string.read_more),
                style = Theme.textStyle.label.medium,
                color = Theme.colors.primary,
                modifier = Modifier.clickable { expanded = true }
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.date_icon),
                contentDescription = stringResource(id = R.string.date),
                tint = Theme.colors.body,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = review.createdDate.toString(),
                style = Theme.textStyle.label.small,
                color = Theme.colors.body
            )
        }
    }
}