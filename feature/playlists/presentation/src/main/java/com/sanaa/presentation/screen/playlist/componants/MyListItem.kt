package com.sanaa.presentation.screen.playlist.componants

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.text.AppText
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.playlists.presentation.R

@Composable
fun MyListItem(
    modifier: Modifier = Modifier,
    onItemClick: () -> Unit = {},
    title: String = stringResource(R.string.my_list),
    count: Int = 10,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Theme.colors.surface,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.dp,
                color = Theme.colors.stroke,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable{ onItemClick() }
            .padding(vertical = 16.dp, horizontal = 12.dp)
    ) {
        AppText(
            modifier = Modifier,
            text = title,
            style = Theme.textStyle.title.medium,
            color = Theme.colors.title,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(8.dp))
                .background(
                    color = Theme.colors.primaryVariant,
                )
                .border(
                    width = 1.dp,
                    color = Theme.colors.stroke,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(vertical = 4.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            AppText(
                modifier = Modifier,
                text = count.toString(),
                style = Theme.textStyle.label.small,
                color = Theme.colors.primary,
                textAlign = TextAlign.Center
            )
            Icon(
                painter = painterResource(R.drawable.icon_arrow_right),
                contentDescription = stringResource(
                    R.string.arrow_right
                ),
                tint = Theme.colors.primary
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun MyListItemPreview() {
    NovixTheme(
        isSystemInDarkTheme()
    ) {
        MyListItem()
    }
}