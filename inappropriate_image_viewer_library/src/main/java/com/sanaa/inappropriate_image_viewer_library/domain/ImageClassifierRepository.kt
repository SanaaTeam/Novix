package com.sanaa.inappropriate_image_viewer_library.domain

import android.graphics.Bitmap

interface ImageClassifierRepository{
    fun classify(bitmap: Bitmap): List<Classification>
}