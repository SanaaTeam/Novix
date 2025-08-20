package com.sanaa.presentation.webview

import kotlinx.serialization.Serializable

interface Destination {
    fun route(): String
}

@Serializable
data class SignUpWebViewRoute(val url: String) : Destination {
    override fun route(): String = "signup?url=$url"
}

@Serializable
data class ForgetPasswordWebViewRoute(val url: String) : Destination {
    override fun route(): String = "forget_password?url=$url"
}