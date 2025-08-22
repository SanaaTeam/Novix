package com.sanaa.tvapp.presentation.screens.searchScreen.componants

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.theme.NovixTheme


@Composable
fun TvTopBar(
    modifier: Modifier = Modifier,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
) {

}

@Preview(device = Devices.TV_1080p, showBackground = true)
@Composable
private fun TvTopBarPrev() {
    NovixTheme(isDarkMode = isSystemInDarkTheme()) {
        TvTopBar(
            selectedTabIndex = 0,
            onTabSelected = {}
        )
    }
}