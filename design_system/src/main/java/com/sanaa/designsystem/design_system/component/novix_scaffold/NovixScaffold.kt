package com.sanaa.designsystem.design_system.component.novix_scaffold

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun NovixScaffold(
    modifier: Modifier = Modifier,
    cancelInnerPadding: Boolean = false,
    floatingActionButton: @Composable () -> Unit = {},
    backgroundColor: Color = Theme.colors.primary,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackBarHost :@Composable ()-> Unit ={},
    contentBackground: Color = Theme.colors.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    backgroundShapes: @Composable () -> Unit = { BackgroundShapes() },
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = topBar,
        bottomBar = bottomBar,
        snackbarHost =snackBarHost,
        floatingActionButton = floatingActionButton,
        contentColor = contentColor,
        containerColor = backgroundColor,
        contentWindowInsets = WindowInsets(0.dp),
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(contentBackground)
                .then(
                    if (cancelInnerPadding) Modifier else Modifier.padding(innerPadding)
                )
        ) {
            backgroundShapes()
            content(innerPadding)
        }
    }
}


@Preview
@Composable
private fun PreviewNovixScaffold() {
    NovixTheme(true) {
        NovixScaffold(
            backgroundShapes = { BackgroundShapes() }
        ) {
            Box(modifier = Modifier.fillMaxSize())
        }
    }
}