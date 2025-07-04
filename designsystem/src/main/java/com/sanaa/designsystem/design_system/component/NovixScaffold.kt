package com.sanaa.designsystem.design_system.component

import android.graphics.BlurMaskFilter
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
            GlowingEquilateralTriangle(
                modifier = Modifier.fillMaxSize(),
                offsetXFraction = -0.35f,
                sideFraction = 1f,
                blurRadius = 1000f
            )

            GlowingEquilateralTriangle(
                modifier = Modifier.fillMaxSize(),
                color = Theme.colors.secondary.copy(alpha = 0.07f),
                offsetXFraction = 0.75f,
                offsetYFraction = 0.25f,
                rotationDegrees = 180f,
                sideFraction = 1.8f
            )

            GlowingEquilateralTriangle(
                modifier = Modifier.fillMaxSize(),
                color = Theme.colors.primary.copy(alpha = 0.03f),
                offsetXFraction = -0.25f,
                offsetYFraction = 0.5f,
                sideFraction = 1f,
                blurRadius = 900f
            )

            GlowingEquilateralTriangle(
                modifier = Modifier.fillMaxSize(),
                color = Theme.colors.primary.copy(alpha = 0.01f),
                offsetXFraction = 0.75f,
                offsetYFraction = 0.75f,
                rotationDegrees = 180f
            )

            content()
        }
    }
}

@Composable
fun GlowingEquilateralTriangle(
    modifier: Modifier = Modifier,
    color: Color = Theme.colors.primary.copy(alpha = 0.07f),
    blurRadius: Float = 600f,
    sideFraction: Float = 0.6f,
    offsetXFraction: Float = 0f,
    offsetYFraction: Float = 0f,
    rotationDegrees: Float = 180f
) {
    Canvas(modifier = modifier) {
        val side = size.width * sideFraction
        val height = (sqrt(3f) / 2f) * side

        val offsetX = size.width * offsetXFraction
        val offsetY = size.height * offsetYFraction

        val centerX = offsetX + side / 2f
        val centerY = offsetY + height / 2f

        val paint = android.graphics.Paint().apply {
            this.color = color.toArgb()
            this.maskFilter = BlurMaskFilter(blurRadius, BlurMaskFilter.Blur.NORMAL)
            this.isAntiAlias = true
        }

        drawIntoCanvas { canvas ->
            canvas.withSave {
                canvas.nativeCanvas.translate(centerX, centerY)
                canvas.nativeCanvas.rotate(rotationDegrees)
                canvas.nativeCanvas.translate(-centerX, -centerY)

                val path = Path().apply {
                    moveTo(offsetX + side / 2f, offsetY)
                    lineTo(offsetX + side, offsetY + height)
                    lineTo(offsetX, offsetY + height)
                    close()
                }

                canvas.nativeCanvas.drawPath(path.asAndroidPath(), paint)
            }
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