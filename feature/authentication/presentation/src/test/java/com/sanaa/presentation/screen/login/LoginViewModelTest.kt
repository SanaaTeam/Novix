package com.sanaa.presentation.screen.login

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import service.StringProvider
import usecase.LoginUseCase

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    private val loginUseCase: LoginUseCase = mockk(relaxed = true)
    private val stringProvider: StringProvider = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: LoginViewModel

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel(loginUseCase, stringProvider, ioDispatcher = testDispatcher)
    }

    @Test
    fun `onUsernameChanged updates username and clears error`() = runTest {
        // Given
        val newUsername = "testuser"

        // When
        viewModel.onUsernameChanged(newUsername)

        // Then
        assertThat(viewModel.state.value.username).isEqualTo(newUsername)
        assertThat(viewModel.state.value.usernameError).isNull()
    }

    @Test
    fun `onPasswordChanged updates password and clears error`() = runTest {
        // Given
        val newPassword = "testpassword"

        // When
        viewModel.onPasswordChanged(newPassword)

        // Then
        assertThat(viewModel.state.value.password).isEqualTo(newPassword)
        assertThat(viewModel.state.value.passwordError).isNull()
    }

    @Test
    fun `onTogglePasswordVisibility toggles password visibility`() = runTest {
        // Given
        val initialVisibility = viewModel.state.value.isPasswordVisible

        // When
        viewModel.onTogglePasswordVisibility()

        // Then
        assertThat(viewModel.state.value.isPasswordVisible).isEqualTo(!initialVisibility)
    }

    @Test
    fun `canSubmit is false when username is empty`() = runTest {
        // Given
        viewModel.onUsernameChanged("")
        viewModel.onPasswordChanged("password")

        // Then
        assertThat(viewModel.state.value.canSubmit).isFalse()
    }

    @Test
    fun `canSubmit is false when password is empty`() = runTest {
        // Given
        viewModel.onUsernameChanged("username")
        viewModel.onPasswordChanged("")

        // Then
        assertThat(viewModel.state.value.canSubmit).isFalse()
    }

    @Test
    fun `canSubmit is true when both username and password are provided`() = runTest {
        // Given
        viewModel.onUsernameChanged("username")
        viewModel.onPasswordChanged("password")

        // Then
        assertThat(viewModel.state.value.canSubmit).isTrue()
    }

    @Test
    fun `onLoginClicked does nothing when canSubmit is false`() = runTest {
        // Given
        viewModel.onUsernameChanged("")
        viewModel.onPasswordChanged("")

        // When
        viewModel.onLoginClicked()

        // Then
        assertThat(viewModel.state.value.isLoading).isFalse()
        viewModel.effect.test {
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onLoginClicked sets loading state when canSubmit is true`() = runTest {
        // Given
        viewModel.onUsernameChanged("username")
        viewModel.onPasswordChanged("password")

        // When
        viewModel.onLoginClicked()

        // Then
        assertThat(viewModel.state.value.isLoading).isTrue()
        assertThat(viewModel.state.value.canSubmit).isFalse()
    }

    @Test
    fun `onForgotPasswordClicked emits NavigateToForgotPassword`() = runTest {
        // When
        viewModel.onForgotPasswordClicked()

        // Then
        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(LoginScreenEffects.NavigateToForgotPassword)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onCreateAccountClicked emits NavigateToCreateAccount`() = runTest {
        // When
        viewModel.onCreateAccountClicked()

        // Then
        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(LoginScreenEffects.NavigateToCreateAccount)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onBackClicked emits NavigateBack`() = runTest {
        // When
        viewModel.onBackClicked()

        // Then
        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(LoginScreenEffects.NavigateBack)
            cancelAndIgnoreRemainingEvents()
        }
    }
    //still we need onLoginClick field

} 