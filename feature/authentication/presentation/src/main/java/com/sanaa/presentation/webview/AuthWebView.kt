package com.sanaa.presentation.webview

import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun AuthWebView(
    webPageUrl: String,
    modifier: Modifier = Modifier,
    onBackPressed: (() -> Unit)? = null,
    onTargetUrlReached: () -> Unit = {},
) {
    val webViewRef = remember { mutableStateOf<WebView?>(null) }

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        if (url == "https://www.themoviedb.org/auth/access/approve") {
                            onTargetUrlReached()
                        }
                    }

                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?,
                    ): Boolean {
                        val requestedUrl = request?.url.toString()
                        if (requestedUrl == "https://www.themoviedb.org/auth/access/approve") {
                            onTargetUrlReached()
                        }
                        return false
                    }
                }
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