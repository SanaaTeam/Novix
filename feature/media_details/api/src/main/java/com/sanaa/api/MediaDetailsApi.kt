package com.sanaa.api

import android.content.Context

interface MediaDetailsApi {

    fun launch(context: Context, startRoute: StartRoute, id: Int)
}

enum class StartRoute {
    SERIES, MOVIE, ACTOR
}