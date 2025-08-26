package com.sanaa.tvapp.presentation.screens.mediaDetails.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.text.AppText
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.tvapp.R

@Composable
fun OverviewSection(
    onReadMore: () -> Unit,
    @StringRes titleResId: Int,
    overview: String,
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
    collapsedMaxLines: Int = 4,
) {
    Column(modifier = modifier) {
        AppText(
            text = stringResource(id = titleResId),
            style = Theme.textStyle.title.medium,
            color = Theme.colors.title
        )

        Spacer(Modifier.height(8.dp))

        ExpandableText(
            text = overview,
            style = Theme.textStyle.body.small,
            color = Theme.colors.body,
            collapsedMaxLines = collapsedMaxLines,
            isExpanded = isExpanded,
            readMoreText = " ${stringResource(R.string.read_more)}",
            readLessText = " ${stringResource(R.string.read_less)}",
            onReadMore = onReadMore
        )
    }
}