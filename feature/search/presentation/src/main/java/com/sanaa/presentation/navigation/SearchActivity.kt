package com.sanaa.presentation.navigation

import com.sanaa.presentation.screen.SearchScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.sanaa.api.MediaDetailsApi
import org.koin.android.ext.android.inject

class SearchActivity : ComponentActivity() {

    private val mediaDetailsNavigator: MediaDetailsApi by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SearchScreen(
                onMediaClick = { mediaId ->
                    mediaDetailsNavigator.launch(this, mediaId)
                }
            )
        }
    }
}