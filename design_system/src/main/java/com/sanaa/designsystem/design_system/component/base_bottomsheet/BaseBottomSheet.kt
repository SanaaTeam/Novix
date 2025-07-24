
package com.sanaa.designsystem.design_system.component.base_bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.theme.Theme

private val ScrimColor = Color(0x99000000)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseBottomSheet(
    onDismiss: () -> Unit,
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    if (isVisible) {
        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )

        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            scrimColor = ScrimColor,
            shape = RoundedCornerShape(24.dp),
            containerColor = Theme.colors.surface,
            dragHandle = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .height(4.dp)
                            .alpha(0.4f)
                            .width(32.dp)
                            .background(
                                color = Theme.colors.body,
                                shape = RoundedCornerShape(100.dp)
                            )
                    )
                }
            },
            content = {
                Box(
                    modifier = modifier
                        .wrapContentHeight()
                ) {
                    content()
                }
            }
        )
    }
}
