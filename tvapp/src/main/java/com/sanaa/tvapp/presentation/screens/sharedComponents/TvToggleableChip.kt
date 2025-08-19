package com.sanaa.tvapp.presentation.screens.sharedComponents


import android.view.KeyEvent.ACTION_UP
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun TvToggleableChip(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    Text(
        modifier = modifier
            .focusable(interactionSource = interactionSource)
            .clickable {
                onClick()
            }
            .onKeyEvent(onKeyEvent = { keyEvent: KeyEvent ->
                if (keyEvent.nativeKeyEvent.action == ACTION_UP){
                    onClick()
                    true
                }else{
                    false
                }
            })
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isSelected)
                    Theme.colors.primary.copy(alpha = if (isFocused) 0.5f else 1f)
                else Theme.colors.surface
            )
            .then(
                if (isFocused) Modifier.border(
                    3.dp,
                    Theme.colors.primary,
                    RoundedCornerShape(12.dp)
                ) else Modifier
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        text = text,
        color = if (isSelected) Theme.colors.onPrimary else Theme.colors.title,
        style = Theme.textStyle.title.medium
    )

}

@PreviewLightDark
@Composable
private fun PreviewCategoryChip() {
    NovixTheme(isSystemInDarkTheme()) {
        var isSelected by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .background(color = Theme.colors.surface)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TvToggleableChip(text = "S1", onClick = {}, isSelected = true)
            TvToggleableChip(
                text = "S1",
                onClick = { isSelected = !isSelected },
                isSelected = isSelected
            )
        }
    }
}