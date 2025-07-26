package com.sanaa.presentation.api

import android.content.Context
import com.sanaa.api.MediaDetailsApi
import com.sanaa.api.SearchNavigatorApi
import com.sanaa.api.StartRoute

class SearchNavigatorApiImpl(
    private val mediaDetailsApi: MediaDetailsApi
) : SearchNavigatorApi {

    override fun navigateToMediaDetails(context: Context, startRoute: StartRoute, id: Int) {
        mediaDetailsApi.launch(context, startRoute, id)
    }
}