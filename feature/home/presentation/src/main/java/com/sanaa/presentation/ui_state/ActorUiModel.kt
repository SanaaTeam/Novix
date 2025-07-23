package com.sanaa.presentation.ui_state

import androidx.compose.ui.graphics.painter.Painter

data class ActorUiModel(
    val id: Int,
    val name: String,
    val character: String? = null,
    val imagePainter: Painter
)
