package com.sanaa.presentation.component

import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.presentation.R

@Composable
fun OverviewSection(
    onReadMore: () -> Unit,
    @StringRes titleResId: Int,
    overview: String,
    modifier: Modifier = Modifier
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            )
    ) {

        Text(
            text = stringResource(id = titleResId),
            style = Theme.textStyle.title.medium,
            color = Theme.colors.title,
        )


        Text(
            text = overview,
            style = Theme.textStyle.body.medium,
            color = Theme.colors.body,
            maxLines = if (isExpanded) Int.MAX_VALUE else 4,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 8.dp)
        )

        Text(
            text = stringResource(if (isExpanded) R.string.read_less else R.string.read_more),
            style = Theme.textStyle.body.medium,
            color = Theme.colors.primary,
            modifier = Modifier
                .padding(top = 4.dp)
                .clickable {
                    isExpanded = !isExpanded
                    if (isExpanded) onReadMore()
                }
        )
    }
}