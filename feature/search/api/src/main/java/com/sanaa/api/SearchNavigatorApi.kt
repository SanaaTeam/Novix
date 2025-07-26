package com.sanaa.api

import android.content.Context

interface SearchNavigatorApi {
    fun navigateToMediaDetails(context: Context, startRoute: StartRoute, id: Int)
}