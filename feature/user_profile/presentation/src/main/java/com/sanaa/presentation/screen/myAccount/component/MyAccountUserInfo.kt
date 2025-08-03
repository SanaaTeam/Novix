package com.sanaa.presentation.screen.myAccount.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.text.AppText
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun MyAccountUserInfo(userName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
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
                modifier = Modifier.size(28.dp),
                painter = painterResource(id = R.drawable.user_profile),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Theme.colors.hint)
            )
        }

        AppText(
            modifier = Modifier.weight(1f),
            text = userName,
            color = Theme.colors.title,
            style = Theme.textStyle.title.medium
        )

        Image(
            modifier = Modifier
                .height(24.dp)
                .clip(RoundedCornerShape(4.dp))
                .clickable {

                },
            painter = painterResource(id = R.drawable.more_vertical),
            contentDescription = null,
            colorFilter = ColorFilter.tint(Theme.colors.body)
        )
    }
}
