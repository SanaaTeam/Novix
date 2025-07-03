package com.sanaa.inappropriate_image_viewer_library.domain

internal data class Classification(
    val label: String,
    val score: Float,
)