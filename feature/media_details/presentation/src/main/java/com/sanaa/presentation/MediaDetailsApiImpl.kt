package com.sanaa.presentation


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.sanaa.api.MediaDetailsApi
import com.sanaa.presentation.screen.MediaDetailsScreen

class MediaDetailsApiImpl : MediaDetailsApi {
    override fun launch(context: Context, mediaId: Int) {
        val intent = Intent(context, MediaDetailsActivity::class.java).apply {
            putExtra(MediaDetailsActivity.EXTRA_MEDIA_ID, mediaId)
        }
        context.startActivity(intent)
    }
}

class MediaDetailsActivity : ComponentActivity() {

    companion object {
        const val EXTRA_MEDIA_ID = "extra_media_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val mediaId = intent.getIntExtra(EXTRA_MEDIA_ID, -1)
        if (mediaId == -1) {
            finish()
            return
        }

        setContent {

            MediaDetailsScreen(
                mediaId = mediaId,
                onBackClick = { finish() }
            )
        }
    }
}