package com.sanaa.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.button.TextButton
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.presentation.model.MediaInfoUiModel
@Composable
fun MediaInfoBox(
    mediaInfo: MediaInfoUiModel,
    onViewReviews: () -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .background(
                color = Theme.colors.surface,
                shape = RoundedCornerShape(16.dp)
            )
            .border(1.dp, Theme.colors.stroke, RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = mediaInfo.title,
                style = Theme.textStyle.title.medium,
                color = Theme.colors.title,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                mediaInfo.genres.forEachIndexed { index, genre ->
                    Text(
                        text = genre,
                        style = Theme.textStyle.label.small,
                        color = Theme.colors.body
                    )
                    if (index < mediaInfo.genres.lastIndex) {
                        Box(
                            modifier = Modifier
                                .size(3.dp)
                                .background(Theme.colors.hint, shape = CircleShape)
                        )
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.btn_star_big_on),
                        contentDescription = "Rating",
                        tint = Theme.colors.statusColors.yellowAccent,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = mediaInfo.rating,
                        style = Theme.textStyle.label.small,
                        color = Theme.colors.title
                    )
                }

                Box(
                    modifier = Modifier
                        .size(3.dp)
                        .background(Theme.colors.body, shape = CircleShape)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_duration),
                        contentDescription = "Duration",
                        tint = Theme.colors.body,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = mediaInfo.duration,
                        style = Theme.textStyle.label.small,
                        color = Theme.colors.body
                    )
                }

                Box(
                    modifier = Modifier
                        .size(3.dp)
                        .background(Theme.colors.body, shape = CircleShape)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_calender),
                        contentDescription = "Release Date",
                        tint = Theme.colors.body,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = mediaInfo.releaseDate,
                        style = Theme.textStyle.label.small,
                        color = Theme.colors.body
                    )
                }
            }

            TextButton(
                text = stringResource(com.sanaa.presentation.R.string.view_reviews),
                onClick = onViewReviews,
                textColor = Theme.colors.primary,
                modifier = Modifier
            )
        }
    }
}
