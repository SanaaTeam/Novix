package com.sanaa.presentation.screen.componants

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun FilterSection(
    onClick: ()->Unit = {}
) {
    Icon(
        painter = painterResource(id = R.drawable.icon_filter),
        contentDescription = stringResource(R.string.icon_filter),
        modifier = Modifier
            .size(width = 52.dp, height = 48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Theme.colors.primary)
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp, horizontal = 16.dp),
        tint = Theme.colors.onPrimary
    )

}

@Preview
@Composable
private fun FilterSectionPreview() {
    FilterSection()
}