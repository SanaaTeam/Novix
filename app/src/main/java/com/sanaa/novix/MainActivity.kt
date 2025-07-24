package com.sanaa.novix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.google.firebase.analytics.FirebaseAnalytics
import com.sanaa.api.SearchFeatureApi
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.android.inject
import timber.log.Timber

class MainActivity : ComponentActivity() {

    private lateinit var analytics: FirebaseAnalytics
    private val searchFeatureApi: SearchFeatureApi by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        analytics = getKoin().get()

        Timber.d("MainActivity created")

        setContent {
            searchFeatureApi.SearchScreen()
        }
    }
}