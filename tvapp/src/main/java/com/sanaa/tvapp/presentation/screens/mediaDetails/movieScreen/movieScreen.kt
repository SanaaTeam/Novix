package com.sanaa.tvapp.presentation.screens.mediaDetails.movieScreen

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.tvapp.presentation.screens.mediaDetails.components.DetailsTopBar

@Composable
fun MovieDetailsScreen(modifier: Modifier = Modifier) {
    NovixTheme (isDarkMode = isSystemInDarkTheme()){
        NovixScaffold(
            modifier = Modifier.padding(top = 40.dp),
            topBar = {
                DetailsTopBar({})
            }
        ) {

        }
    }
}