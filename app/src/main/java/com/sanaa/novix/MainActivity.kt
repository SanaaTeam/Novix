package com.sanaa.novix

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.google.firebase.analytics.FirebaseAnalytics
import com.sanaa.presentation.navigation.SearchActivity
import org.koin.android.ext.android.getKoin
import timber.log.Timber

class MainActivity : ComponentActivity() {

    private lateinit var analytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        analytics = getKoin().get()

        Timber.d("MainActivity created")

        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
        finish()
    }
}