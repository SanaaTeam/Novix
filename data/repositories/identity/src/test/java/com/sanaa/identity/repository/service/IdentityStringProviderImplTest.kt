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
        // Setup mock responses for getString
        every { context.getString(R.string.welcome_back) } returns "Welcome back!"
        every { context.getString(R.string.no_internet_connection_error) } returns "No internet connection."
        every { context.getString(R.string.invalid_username_password_error) } returns "Invalid username or password."
        every { context.getString(R.string.enter_username_password_error) } returns "Please enter username and password."
        every { context.getString(R.string.something_went_wrong_error) } returns "Something went wrong."

        stringProvider = IdentityStringProviderImpl(context)
    }

    @Test
    fun `welcomeBack returns expected string`() {
        assertEquals(stringProvider.welcomeBack, "Welcome back!")
    }

    @Test
    fun `noInternetConnectionError returns expected string`() {
        assertEquals("No internet connection.", stringProvider.noInternetConnectionError)
    }

    @Test
    fun `invalidUserNameAndPasswordError returns expected string`() {
        assertEquals(
            "Invalid username or password.",
            stringProvider.invalidUserNameAndPasswordError
        )
    }

    @Test
    fun `enterUserNameAndPasswordError returns expected string`() {
        assertEquals(
            "Please enter username and password.",
            stringProvider.enterUserNameAndPasswordError
        )
    }

    @Test
    fun `somethingWentWrongError returns expected string`() {
        assertEquals("Something went wrong.", stringProvider.somethingWentWrongError)
    }
}
