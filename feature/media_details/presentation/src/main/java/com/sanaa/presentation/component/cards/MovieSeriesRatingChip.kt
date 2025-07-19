package com.sanaa.presentation.component.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun MovieSeriesRatingChip(
    rating: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(32.dp)
            .border(
                width = 1.dp,
                color = Theme.colors.stroke,
                shape = RoundedCornerShape(8.dp)
            )
            .background(
                color = Theme.colors.iconBackground,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.icon_star),
            contentDescription = null,
            modifier = Modifier.size(12.dp),
            tint = Theme.colors.statusColors.yellowAccent
        )
        Text(
            text = rating,
            style = Theme.textStyle.label.small,
            color = Theme.colors.onPrimary
        )
    }
}

@Preview
@Composable
private fun PreviewMovieSeriesRatingChip() {
    NovixTheme(isSystemInDarkTheme()) {
        Column(
            modifier = Modifier
                .background(color = Theme.colors.surface)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MovieSeriesRatingChip(rating = "9.9")
        }
    }
}