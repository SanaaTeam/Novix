package com.sanaa.presentation.navigation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.sanaa.api.MediaDetailsApi
import com.sanaa.api.StartRoute

class MediaDetailsApiImpl : MediaDetailsApi {
    override fun launch(context: Context, startRoute: StartRoute, id: Int) {
        val intent = Intent(context, MediaDetailsActivity::class.java).apply {
            putExtra(MediaDetailsActivity.EXTRA_START_ROUTE, startRoute.name)
            putExtra(MediaDetailsActivity.EXTRA_MEDIA_ID, id)
        }
        context.startActivity(intent)
    }
}

class MediaDetailsActivity : ComponentActivity() {

    companion object {
        const val EXTRA_START_ROUTE = "extra_start_route"
        const val EXTRA_MEDIA_ID = "extra_media_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val startRouteName = intent.getStringExtra(EXTRA_START_ROUTE)
        val mediaId = intent.getIntExtra(EXTRA_MEDIA_ID, -1)

        if (startRouteName == null || mediaId == -1) {
            finish()
            return
        }

        val startRoute = StartRoute.valueOf(startRouteName)

        setContent {
            DetailsNavHost(
                startRoute = startRoute,
                id = mediaId
            )
        }
    }
}