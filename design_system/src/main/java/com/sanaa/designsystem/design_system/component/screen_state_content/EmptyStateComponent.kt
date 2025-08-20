package com.sanaa.designsystem.design_system.component.screen_state_content

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.text.AppText
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun EmptyStateComponent(
    messageText: String,
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
    ) {
        Image(
            painter = painterResource(R.drawable.no_items),
            contentDescription = null,
            modifier = Modifier
                .padding(bottom = 12.dp)
                .size(128.dp),
            contentScale = ContentScale.Fit
        )
        AppText(
            text = messageText,
            style = Theme.textStyle.body.small,
            color = Theme.colors.body,
            textAlign = TextAlign.Center
        )
    }
}

