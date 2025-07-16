package com.sanaa.api

import android.content.Context

interface MediaDetailsApi {
    fun launch(context: Context, mediaId: Int)
}