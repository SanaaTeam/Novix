package com.sanaa.presentation.screen.componants

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun FilterSection(
    onClick: ()->Unit = {}
) {
    Box(
        modifier = Modifier
            .size(height = 48.dp, width = 52.dp)
            .background(
                color = Theme.colors.primary,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.icon_filter),
            contentDescription = null,
            modifier = Modifier
                .size(20.dp),
            tint = Theme.colors.onPrimary
        )
    }
}

@Preview
@Composable
private fun FilterSectionPreview() {
    FilterSection()
}