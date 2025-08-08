package com.sanaa.presentation.screen.changePassword

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun WebView(
    webPageUrl: String,
    modifier: Modifier = Modifier,
    onBackPressed: (() -> Unit)? = null,
) {
    val webViewRef = remember { mutableStateOf<WebView?>(null) }

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                webViewClient = WebViewClient()
                loadUrl(webPageUrl)
                webViewRef.value = this
            }
        },
        modifier = modifier
    )

    BackHandler {
        val webView = webViewRef.value
        if (webView?.canGoBack() == true) {
            webView.goBack()
        } else {
            onBackPressed?.invoke()
        }
    }
}