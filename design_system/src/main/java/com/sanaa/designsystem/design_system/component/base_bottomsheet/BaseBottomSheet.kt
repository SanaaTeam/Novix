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
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.theme.Theme
import androidx.compose.ui.draw.alpha

private val ScrimColor = Color(0x99000000)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseBottomSheet(
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    onDismiss: () -> Unit,
) {
    LaunchedEffect(Unit) {
        sheetState.expand()
    }
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss,
        scrimColor = ScrimColor,
        shape = RoundedCornerShape(24.dp, 24.dp),
        containerColor = Theme.colors.surface,
        dragHandle = {
            Row(
                modifier = modifier
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
                modifier = Modifier.wrapContentHeight()
                    .padding(bottom = 24.dp, start = 16.dp, end = 16.dp),
            )
            {
                content()
            }
        }
    )
}