package com.sanaa.tvapp.resourceProvider

import android.content.Context
import com.sanaa.tvapp.R
import dagger.hilt.android.qualifiers.ApplicationContext
import service.StringProvider
import javax.inject.Inject

class StringProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : StringProvider {
    override val welcomeBack: String
        get() = context.getString(R.string.welcome_back)

    override val noInternetConnectionError: String
        get() = context.getString(R.string.no_internet_connection_error)

    override val invalidUserNameAndPasswordError: String
        get() = context.getString(R.string.invalid_username_password_error)

    override
    val enterUserNameAndPasswordError: String
        get() = context.getString(R.string.enter_username_password_error)

    override val somethingWentWrongError: String
        get() = context.getString(R.string.something_went_wrong_error)
}