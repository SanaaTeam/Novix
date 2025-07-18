package com.sanaa.presentation.screen.componants

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun RecentSearchItem(
    text: String,
    modifier: Modifier = Modifier,
    onDeleteClicked: () -> Unit = {},
    onRecentSearchItemClicked: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable(
                    onClick = onRecentSearchItemClicked
                )
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .weight(1f)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_clock),
                contentDescription = null,
                tint = Theme.colors.hint,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = text,
                style = Theme.textStyle.body.medium,
                color = Theme.colors.title,
            )
        }
        Icon(
            painter = painterResource(id = R.drawable.icon_cancel),
            contentDescription = null,
            tint = Theme.colors.hint,
            modifier = Modifier
                .clickable(onClick = onDeleteClicked)
                .padding(16.dp)
                .size(16.dp)
        )
    }


}

@PreviewLightDark
@Composable
private fun RecentSearchItemPreviewDark() {
    NovixTheme(isSystemInDarkTheme()) {
        RecentSearchItem("Shutter island")
    }
}