package com.sanaa.novix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.sanaa.api.*
import com.sanaa.presentation.app.NovixApp
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var homeFeatureApi: HomeFeatureApi
    @Inject
    lateinit var searchFeatureApi: SearchFeatureApi

    @Inject
    lateinit var mediaDetailsApi: MediaDetailsApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Timber.d("MainActivity created")

        setContent {
            NovixApp(
                homeFeatureApi = homeFeatureApi,
                searchFeatureApi = searchFeatureApi,
                mediaDetailsApi = mediaDetailsApi
            )
        }
    }
}