package com.sanaa.presentation.navigation

import kotlinx.serialization.Serializable

open class DestinationLogin

@Serializable
class WelcomeRoute : DestinationLogin()

@Serializable
object LoginRoute : DestinationLogin()

@Serializable
object SignUpRoute : DestinationLogin()

@Serializable
data class ApproveAccessToken(val requestToken: String) : DestinationLogin()

@Serializable
object ForgetPasswordRoute : DestinationLogin()