package com.sanaa.presentation.screen.componants

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun RecentSearchItem(
    text: String = stringResource(R.string.shutter_island),
    onDeleteClicked: () -> Unit = {},
    onRecentSearchItemClicked: () -> Unit = {}
) {

    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 12.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically

    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(1f)
                .clickable(
                    onClick = onRecentSearchItemClicked
                )
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
                .size(16.dp)
                .clickable(onClick = onDeleteClicked)
        )
    }


}

@Preview(showBackground = true, locale = "en")
@Composable
private fun RecentSearchItemPreviewLight() {
    NovixTheme(false) {
        RecentSearchItem()
    }
}

@Preview(showBackground = true, locale = "en")
@Composable
private fun RecentSearchItemPreviewDark() {
    NovixTheme(true) {
        RecentSearchItem()
    }
}