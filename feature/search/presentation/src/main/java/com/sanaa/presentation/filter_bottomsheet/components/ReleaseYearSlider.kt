package com.sanaa.presentation.filter_bottomsheet.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.slider.YearSlider
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun CustomYearRangeSlider(
    modifier: Modifier = Modifier,
    title: String,
    value: ClosedFloatingPointRange<Float>,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
    onDragStateChanged: (isDragging: Boolean) -> Unit,
    valueRange: ClosedFloatingPointRange<Float> = 1980f..2025f,
    steps: Int = (2025 - 1980) - 1,
) {

    Column(modifier.fillMaxWidth()) {
        BasicText(
            text = title,
            style = Theme.textStyle.title.small.copy(color = Theme.colors.title),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 3.dp, end = 3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            YearTitle(value.start.toInt().toString())
            Spacer(Modifier.weight(1f))
            YearTitle(value.endInclusive.toInt().toString())
        }

        YearSlider(
            value,
            valueRange,
            steps,
            onDragStateChanged,
            onValueChange,
        )
    }
}

@Composable
private fun YearTitle(year: String) {
    BasicText(
        text = year,
        style = Theme.textStyle.label.small.copy(color = Theme.colors.body),
    )
}


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun CustomYearRangeSliderPreview() {
    var sliderPosition by remember { mutableStateOf(1995f..2012f) }

    Column(
        modifier = Modifier
            .background(Theme.colors.surface)
            .padding(16.dp)
    ) {
        CustomYearRangeSlider(
            title = "Released year",
            value = sliderPosition,
            onValueChange = { newPosition ->
                sliderPosition = newPosition
            },
            onDragStateChanged = {}
        )
    }
}