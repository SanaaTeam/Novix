package com.sanaa.presentation.navigation

import kotlinx.serialization.Serializable

interface DestinationLogin {
    fun route(): String
}

@Serializable object WelcomeRoute : DestinationLogin {
    override fun route() = "welcome"
    const val PATTERN = "welcome"
}

@Serializable object LoginRoute : DestinationLogin {
    override fun route() = "login"
    const val PATTERN = "login"
}