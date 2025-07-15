package com.sanaa.presentation.component

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.designsystem.design_system.theme.text_style.LocalNovixTextStyle

@Composable
fun OverviewSection(
    onReadMore: () -> Unit,
    overview: String,
    modifier: Modifier = Modifier
) {
    val textStyle = LocalNovixTextStyle.current
    var isExpanded by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
    ) {
        Text(
            text = "Overview",
            style = textStyle.title.medium,
            color = Theme.colors.title
        )

        Text(
            text = overview,
            style = textStyle.body.medium,
            color = Theme.colors.body,
            maxLines = if (isExpanded) Int.MAX_VALUE else 4,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 8.dp)
        )

        Text(
            text = if (isExpanded) "Read less" else "Read more",
            style = textStyle.body.medium,
            color = Theme.colors.primary,
            modifier = Modifier.clickable(
                onClick = {
                isExpanded = !isExpanded
                if (isExpanded) onReadMore()
            }
            )
        )
    }
}