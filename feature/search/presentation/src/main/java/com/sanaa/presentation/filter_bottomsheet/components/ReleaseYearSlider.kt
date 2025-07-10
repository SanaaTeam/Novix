package com.sanaa.presentation.filter_bottomsheet.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.RangeSliderState
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.theme.Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomYearRangeSlider(
    modifier: Modifier = Modifier,
    title: String,
    value: ClosedFloatingPointRange<Float>,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
    valueRange: ClosedFloatingPointRange<Float> = 1980f..2025f,
    steps: Int = (2025 - 1980) - 1,
) {
    val activeColor = Theme.colors.primary
    val inactiveColor = Theme.colors.surfaceHigh

    val customSliderColors = SliderDefaults.colors(
        thumbColor = activeColor,
        activeTrackColor = activeColor,
        inactiveTrackColor = inactiveColor
    )

    Column(modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = Theme.textStyle.title.small,
            color = Theme.colors.title
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 3.dp, end = 3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = value.start.toInt().toString(),
                style = Theme.textStyle.label.small,
                color = Theme.colors.body
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = value.endInclusive.toInt().toString(),
                style = Theme.textStyle.label.small,
                color = Theme.colors.body
            )
        }

        val startInteractionSource = remember { MutableInteractionSource() }
        val endInteractionSource = remember { MutableInteractionSource() }

        RangeSlider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            steps = steps,
            colors = customSliderColors,
            startInteractionSource = startInteractionSource,
            endInteractionSource = endInteractionSource,
            startThumb = {
                CircularThumb(
                    interactionSource = startInteractionSource
                )
            },
            endThumb = {
                CircularThumb(
                    interactionSource = endInteractionSource
                )
            },
            track = { rangeSliderState: RangeSliderState ->
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
        )
    }
}

@Composable
private fun CircularThumb(
    interactionSource: MutableInteractionSource
) {
    Spacer(
        Modifier
            .size(16.dp)
            .hoverable(interactionSource = interactionSource)
            .border(1.dp, Theme.colors.stroke, CircleShape)
            .background(Theme.colors.primary, CircleShape)
    )
}


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun CustomYearRangeSliderPreview() {
    var sliderPosition by remember { mutableStateOf(1995f..2012f) }

    Column(modifier = Modifier.padding(0.dp)) {
        CustomYearRangeSlider(
            title = "Released year",
            value = sliderPosition,
            onValueChange = { newPosition ->
                sliderPosition = newPosition
            }
        )
    }
}
