package com.sanaa.presentation.screen.genreMovies.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon

@Composable
fun GenreMoviesTopBar(
    title: String,
    onBackClick: () -> Unit,
) {
    TopBar(
        leftContent = {
            TopBarClickableIcon(
                icon = painterResource(id = R.drawable.icon_back),
                onClick = onBackClick
            )
        },
        screenTitle = title,
        modifier = Modifier
            .fillMaxWidth()
            .systemBarsPadding()
    )
}