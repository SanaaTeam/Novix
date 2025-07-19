package com.sanaa.presentation.ui

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.sanaa.api.MediaDetailsApi
import com.sanaa.api.StartRoute
import com.sanaa.presentation.navigation.DetailsNavHost

class MediaDetailsApiImpl : MediaDetailsApi {
    override fun launch(context: Context, startRoute: StartRoute, id: Int) {
        val intent = MediaDetailsActivity.createIntent(context, startRoute, id)
        context.startActivity(
            intent,
            ActivityOptions.makeSceneTransitionAnimation(context as? ComponentActivity).toBundle()
        )
    }
}


class MediaDetailsActivity : ComponentActivity() {

    companion object {
        private const val EXTRA_START_ROUTE = "extra_start_route"
        private const val EXTRA_MEDIA_ID = "extra_media_id"

        fun createIntent(context: Context, startRoute: StartRoute, id: Int): Intent {
            return Intent(context, MediaDetailsActivity::class.java).apply {
                putExtra(EXTRA_START_ROUTE, startRoute.name)
                putExtra(EXTRA_MEDIA_ID, id)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val startRoute = intent.getStringExtra(EXTRA_START_ROUTE)?.let { StartRoute.valueOf(it) }
        val mediaId = intent.getIntExtra(EXTRA_MEDIA_ID, -1)

        if (startRoute == null || mediaId == -1) {
            finish()
            return
        }

        setContent {
            DetailsNavHost(startRoute = startRoute, id = mediaId)
        }
    }
}