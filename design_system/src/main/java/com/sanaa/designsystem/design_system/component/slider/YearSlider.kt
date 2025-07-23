package com.sanaa.designsystem.design_system.component.slider

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.RangeSliderState
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun YearSlider(
    value: ClosedFloatingPointRange<Float>,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int = 1,
    onDragStateChanged: (isDragging: Boolean) -> Unit = {},
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit = {},
) {
    val activeColor = Theme.colors.primary
    val inactiveColor = Theme.colors.surfaceHigh

    val customSliderColors = SliderDefaults.colors(
        thumbColor = activeColor,
        activeTrackColor = activeColor,
        inactiveTrackColor = inactiveColor
    )
    Box(
        modifier = Modifier.pointerInput(Unit) {
            detectDragGestures(
                onDragStart = { _ -> onDragStateChanged(true) },
                onDragEnd = { onDragStateChanged(false) },
                onDragCancel = { onDragStateChanged(false) },
                onDrag = { _, _ -> }
            )
        }
    ) {
        RangeSlider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            steps = steps,
            colors = customSliderColors,
            startThumb = {
                CircularThumb()
            },
            endThumb = {
                CircularThumb()
            },
            track = { rangeSliderState: RangeSliderState ->
                YearSliderTrack(inactiveColor, rangeSliderState, activeColor)
            }
        )
    }
}

@Composable
private fun CircularThumb() {
    Spacer(
        Modifier
            .size(16.dp)
            .border(1.dp, Theme.colors.stroke, CircleShape)
            .background(Theme.colors.primary, CircleShape)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun YearSliderTrack(
    inactiveColor: Color,
    rangeSliderState: RangeSliderState,
    activeColor: Color,
) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
    ) {
        val trackStartY = center.y
        val trackEndX = size.width
        val trackStrokeWidth = 8.dp.toPx()

        drawLine(
            color = inactiveColor,
            start = Offset(0f, trackStartY),
            end = Offset(trackEndX, trackStartY),
            strokeWidth = trackStrokeWidth,
            cap = StrokeCap.Round
        )

        val range =
            rangeSliderState.valueRange.endInclusive - rangeSliderState.valueRange.start
        if (range > 0) {
            val activeStartFraction =
                (rangeSliderState.activeRangeStart - rangeSliderState.valueRange.start) / range
            val activeEndFraction =
                (rangeSliderState.activeRangeEnd - rangeSliderState.valueRange.start) / range

            val activeStartPx = activeStartFraction * trackEndX
            val activeEndPx = activeEndFraction * trackEndX

            if (layoutDirection == LayoutDirection.Rtl) {
                drawLine(
                    color = activeColor,
                    start = Offset(trackEndX - activeEndPx, trackStartY),
                    end = Offset(trackEndX - activeStartPx, trackStartY),
                    strokeWidth = trackStrokeWidth,
                    cap = StrokeCap.Round
                )
            } else {
                drawLine(
                    color = activeColor,
                    start = Offset(activeStartPx, trackStartY),
                    end = Offset(activeEndPx, trackStartY),
                    strokeWidth = trackStrokeWidth,
                    cap = StrokeCap.Round
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewYearSlider() {
    NovixTheme(isSystemInDarkTheme()) {
        YearSlider(
            1980f..2025f,
            1980f..2025f
        )
    }
}