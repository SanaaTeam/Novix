package com.sanaa.presentation.screen.myAccount.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.theme.Theme

data class AccountOptionItem(
    val painter: Painter,
    val title: String,
    val onClick: () -> Unit,
)

@Composable
fun AccountOption(painter: Painter, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clickable {
                onClick()
            }
            .padding(vertical = 16.dp, horizontal = 16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Theme.colors.iconBackgroundLow)
                .border(
                    1.dp,
                    Theme.colors.stroke,
                    RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier.size(24.dp),
                painter = painter,
                contentDescription = title,
                colorFilter = ColorFilter.tint(Theme.colors.primary)
            )
        }

        Text(
            modifier = Modifier,
            text = title,
            color = Theme.colors.title,
            style = Theme.textStyle.title.medium
        )
    }
}