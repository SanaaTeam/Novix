package com.sanaa.presentation.screen.series.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.button.NovixTextButton
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.mediadetails.presentation.R
import com.sanaa.presentation.shared_component.DotSeparator
import com.sanaa.presentation.shared_component.IconWithText
import com.sanaa.presentation.shared_component.ImageSlider
import com.sanaa.presentation.shared_component.InfoSection
import com.sanaa.presentation.model.GenreUiModel

@Composable
fun SeriesHeaderSection(
    title: String,
    rating: String?,
    season: String,
    airDate: String?,
    imagesUrl: List<String>,
    genres: List<GenreUiModel>,
    modifier: Modifier = Modifier,
    onReviewClicked: () -> Unit = {},
    showReviews: Boolean = true,
    onGenreClicked: (GenreUiModel) -> Unit = {}
) {
    Box(
        modifier = modifier
    ) {
        ImageSlider(
            images = imagesUrl,
            modifier = Modifier.align(Alignment.TopCenter),
            contentDescription = null
        )
        InfoSection(
            title = title,
            modifier = Modifier
                .padding(top = 208.dp, start = 16.dp, end = 16.dp)
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
            ) {
                FlowRow {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        genres.forEachIndexed { index, genre ->
                            Text(
                                text = genre.name,
                                style = Theme.textStyle.label.small,
                                color = Theme.colors.body,
                                modifier = Modifier
                                    .clickable { onGenreClicked(genre) }
                            )
                            if (index != genres.lastIndex) {
                                DotSeparator()
                            }
                        }
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    rating?.let {
                        IconWithText(
                            iconRes = R.drawable.icon_star,
                            text = rating,
                            contentDescription = rating,
                            tint = Theme.colors.statusColors.yellowAccent
                        )

                        DotSeparator()
                    }
                    airDate?.let {
                        IconWithText(
                            text = airDate,
                            iconRes = R.drawable.icon_calender,
                            contentDescription = airDate,
                            tint = Theme.colors.hint
                        )

                        DotSeparator()
                    }
                    IconWithText(
                        text = season,
                        iconRes = R.drawable.icon_seasons,
                        contentDescription = season,
                        tint = Theme.colors.hint
                    )
                }
                if (showReviews) {
                    NovixTextButton(
                        text = stringResource(R.string.view_reviews),
                        onClick = onReviewClicked
                    )
                }
            }
        }
    }
}