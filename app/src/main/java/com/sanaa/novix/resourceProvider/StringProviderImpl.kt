package com.sanaa.novix.resourceProvider

import android.content.Context
import com.sanaa.novix.R
import service.StringProvider

class StringProviderImpl(private val context: Context) : StringProvider {
    override val welcomeBack: String
        get() = context.getString(R.string.welcome_back)

    override
    val enterUserNameAndPasswordError: String
        get() = context.getString(R.string.enter_username_password_error)
}