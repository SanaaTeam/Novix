package com.sanaa.presentation.screen.movieDetails.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.button.TextButton
import com.sanaa.designsystem.design_system.component.text.AppText
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.mediadetails.presentation.R
import com.sanaa.presentation.modifier.fillWidthOfParent
import com.sanaa.presentation.screen.movieDetails.MovieDetailsScreenInteractionListener
import com.sanaa.presentation.screen.movieDetails.MovieDetailsUiState
import com.sanaa.presentation.shared_component.DotSeparator
import com.sanaa.presentation.shared_component.IconWithText
import com.sanaa.presentation.shared_component.ImageSlider
import com.sanaa.presentation.shared_component.InfoSection
import com.sanaa.presentation.util.DateTimeUtils.formatTime
import java.util.Locale
import com.sanaa.designsystem.R  as designR
@Composable
fun MovieHeaderSection(
    state: MovieDetailsUiState,
    interactionListener: MovieDetailsScreenInteractionListener,
    locale: Locale
) {
    Box(modifier = Modifier.fillWidthOfParent(16.dp)) {
        ImageSlider(
            images = state.imagesUrls,
            contentDescription = state.movieDetails.title,
            modifier = Modifier.fillMaxWidth()
        )
        InfoSection(
            title = state.movieDetails.title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 208.dp)
                .padding(horizontal = 16.dp)
        ) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                state.movieDetails.genres.forEachIndexed { index, genre ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            interactionListener.onGenreClicked(genre)
                        }
                    ) {
                        AppText(
                            text = genre.name,
                            style = Theme.textStyle.label.small,
                            color = Theme.colors.body,
                        )
                        if (index < state.movieDetails.genres.lastIndex) {
                            DotSeparator()
                        }
                    }
                }
            }
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    state.movieDetails.rating?.let {
                        IconWithText(
                            iconRes = designR.drawable.star,
                            contentDescription = null,
                            textColor = Theme.colors.title,
                            text = state.movieDetails.rating,
                            tint = Theme.colors.statusColors.yellowAccent
                        )
                        DotSeparator()
                    }
                    state.movieDetails.duration?.let { duration ->
                        IconWithText(
                            iconRes = R.drawable.icon_duration,
                            contentDescription = null,
                            text = formatTime(duration, locale),
                            tint = Theme.colors.body
                        )
                        DotSeparator()
                    }
                    IconWithText(
                        iconRes = R.drawable.icon_calender,
                        contentDescription = null,
                        text = state.movieDetails.releaseDate,
                        tint = Theme.colors.body
                    )
                }
            }
            TextButton(
                text = stringResource(id = R.string.view_reviews),
                textColor = Theme.colors.primary,
                onClick = { interactionListener.onShowReviewsClick(state.movieDetails.id) }
            )
        }
    }
}