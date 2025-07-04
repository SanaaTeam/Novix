package com.sanaa.designsystem.design_system.component.novix_scaffold

import android.graphics.BlurMaskFilter
import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.withSave
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import kotlin.math.sqrt

@Composable
fun NovixScaffold(
    modifier: Modifier = Modifier,
    floatingActionButton: @Composable () -> Unit = {},
    backgroundColor: Color = Theme.colors.primary,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    contentBackground: Color = Theme.colors.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    backgroundShapes:  @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = { topBar() },
        bottomBar = bottomBar,
        floatingActionButton = floatingActionButton,
        contentColor = contentColor,
        containerColor = backgroundColor,
        contentWindowInsets = WindowInsets(0.dp)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(contentBackground)
                .padding(innerPadding)
        ) {
           backgroundShapes()
            content()
        }
    }
}


@Preview
@Composable
private fun PreviewNovixScaffold() {
    NovixTheme(true) {
        NovixScaffold {
            Box(modifier = Modifier.fillMaxSize())
        }
    }
}