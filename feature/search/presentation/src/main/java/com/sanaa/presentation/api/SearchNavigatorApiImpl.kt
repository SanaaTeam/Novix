package com.sanaa.presentation.api

import android.content.Context
import com.sanaa.api.MediaDetailsApi
import com.sanaa.api.SearchNavigatorApi
import com.sanaa.api.StartRoute
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchNavigatorApiImpl @Inject constructor(
    private val mediaDetailsApi: MediaDetailsApi
) : SearchNavigatorApi {

    override fun navigateToMediaDetails(context: Context, startRoute: StartRoute, id: Int) {
        mediaDetailsApi.launch(context, startRoute, id)
    }
}