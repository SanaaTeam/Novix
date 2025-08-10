package com.sanaa.identity.repository.service

import android.content.Context
import com.sanaa.identity.R
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class IdentityStringProviderImplTest {

    private val context: Context = mockk(relaxed = true)
    private lateinit var stringProvider: IdentityStringProviderImpl

    @BeforeEach
    fun setUp() {

        every { context.getString(R.string.welcome_back) } returns WELCOME_BACK
        every { context.getString(R.string.no_internet_connection_error) } returns NO_INTERNET_CONNECTION_ERROR
        every { context.getString(R.string.invalid_username_password_error) } returns INVALID_USER_NAME_AND_PASSWORD_ERROR
        every { context.getString(R.string.enter_username_password_error) } returns ENTER_USER_NAME_AND_PASSWORD_ERROR
        every { context.getString(R.string.something_went_wrong_error) } returns SOMETHING_WENT_WRONG_ERROR

        stringProvider = IdentityStringProviderImpl(context)
    }

    @Test
    fun `welcomeBack should return expected string`() {
        assertEquals(stringProvider.welcomeBack, WELCOME_BACK)
    }

    @Test
    fun `noInternetConnectionError should return expected string`() {
        assertEquals(NO_INTERNET_CONNECTION_ERROR, stringProvider.noInternetConnectionError)
    }

    @Test
    fun `invalidUserNameAndPasswordError should return expected string`() {
        assertEquals(
            INVALID_USER_NAME_AND_PASSWORD_ERROR,
            stringProvider.invalidUserNameAndPasswordError
        )
    }

    @Test
    fun `enterUserNameAndPasswordError should return expected string`() {
        assertEquals(
            ENTER_USER_NAME_AND_PASSWORD_ERROR,
            stringProvider.enterUserNameAndPasswordError
        )
    }

    @Test
    fun `somethingWentWrongError should return expected string`() {
        assertEquals(SOMETHING_WENT_WRONG_ERROR, stringProvider.somethingWentWrongError)
    }

    private companion object {
        const val WELCOME_BACK = "Welcome back!"
        const val NO_INTERNET_CONNECTION_ERROR = "No internet connection."
        const val INVALID_USER_NAME_AND_PASSWORD_ERROR = "Invalid username or password."
        const val ENTER_USER_NAME_AND_PASSWORD_ERROR = "Please enter username and password."
        const val SOMETHING_WENT_WRONG_ERROR = "Something went wrong."
    }
}
