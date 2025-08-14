package com.sanaa.presentation.screen.login

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import exceptions.InvalidUserOrPasswordException
import exceptions.NoInternetException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import service.IdentityStringProvider
import usecase.LoginUseCase

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    private val loginUseCase: LoginUseCase = mockk(relaxed = true)
    private val identityStringProvider: IdentityStringProvider = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: LoginViewModel

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel(loginUseCase, identityStringProvider, ioDispatcher = testDispatcher)
    }

    @Test
    fun `onUsernameChanged updates username and clears error`() = runTest {
        val newUsername = "test-user"
        viewModel.onUsernameChanged(newUsername)
        assertThat(viewModel.state.value.username).isEqualTo(newUsername)
        assertThat(viewModel.state.value.usernameError).isNull()
    }

    @Test
    fun `onPasswordChanged updates password and clears error`() = runTest {
        val newPassword = "test-password"
        viewModel.onPasswordChanged(newPassword)
        assertThat(viewModel.state.value.password).isEqualTo(newPassword)
        assertThat(viewModel.state.value.passwordError).isNull()
    }

    @Test
    fun `onTogglePasswordVisibility toggles password visibility`() = runTest {
        val initialVisibility = viewModel.state.value.isPasswordVisible
        viewModel.onTogglePasswordVisibility()
        assertThat(viewModel.state.value.isPasswordVisible).isEqualTo(!initialVisibility)
    }

    @Test
    fun `canSubmit is false when username is empty`() = runTest {
        viewModel.onUsernameChanged("")
        viewModel.onPasswordChanged("password")
        assertThat(viewModel.state.value.canSubmit).isFalse()
    }

    @Test
    fun `canSubmit is false when password is empty`() = runTest {
        viewModel.onUsernameChanged("username")
        viewModel.onPasswordChanged("")
        assertThat(viewModel.state.value.canSubmit).isFalse()
    }

    @Test
    fun `canSubmit is true when both username and password are provided`() = runTest {
        viewModel.onUsernameChanged("username")
        viewModel.onPasswordChanged("password")
        assertThat(viewModel.state.value.canSubmit).isTrue()
    }

    @Test
    fun `onLoginClicked does nothing when canSubmit is false`() = runTest {
        viewModel.onUsernameChanged("")
        viewModel.onPasswordChanged("")
        viewModel.onLoginClicked()
        assertThat(viewModel.state.value.isLoading).isFalse()
        viewModel.effect.test {
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onLoginClicked sets loading state when canSubmit is true`() = runTest {
        viewModel.onUsernameChanged("username")
        viewModel.onPasswordChanged("password")
        viewModel.onLoginClicked()
        assertThat(viewModel.state.value.isLoading).isTrue()
        assertThat(viewModel.state.value.canSubmit).isFalse()
    }

    @Test
    fun `onForgotPasswordClicked emits NavigateToForgotPassword`() = runTest {
        viewModel.onForgotPasswordClicked()
        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(LoginScreenEffects.NavigateToForgotPassword)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onCreateAccountClicked emits NavigateToCreateAccount`() = runTest {
        viewModel.onCreateAccountClicked()
        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(LoginScreenEffects.NavigateToCreateAccount)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onBackClicked emits NavigateBack`() = runTest {
        viewModel.onBackClicked()
        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(LoginScreenEffects.NavigateBack)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onLoginClicked with valid credentials calls loginUseCase and navigates to home`() = runTest {
        val username = "test-user"
        val password = "test-pass"
        viewModel.onUsernameChanged(username)
        viewModel.onPasswordChanged(password)
        viewModel.onLoginClicked()

        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(LoginScreenEffects.ReturnLoggedInResultCode)
            cancelAndIgnoreRemainingEvents()
        }
        assertThat(viewModel.state.value.isLoading).isFalse()
        assertThat(viewModel.state.value.canSubmit).isTrue()
    }


    @Test
    fun `onDataLoadError handles NoInternetException`() = runTest {
        val errorMessage = "No internet connection"
        coEvery { identityStringProvider.noInternetConnectionError } returns errorMessage
        val exception = NoInternetException()
        viewModel.onDataLoadError(exception)

        viewModel.effect.test {
            val effect = awaitItem()
            assertThat(effect).isInstanceOf(LoginScreenEffects.ShowError::class.java)
            assertThat((effect as LoginScreenEffects.ShowError).message).isEqualTo(errorMessage)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onDataLoadError handles unknown exception`() = runTest {
        val errorMessage = "Something went wrong"
        coEvery { identityStringProvider.somethingWentWrongError } returns errorMessage
        val exception = RuntimeException("Unknown error")
        viewModel.onDataLoadError(exception)

        viewModel.effect.test {
            val effect = awaitItem()
            assertThat(effect).isInstanceOf(LoginScreenEffects.ShowError::class.java)
            assertThat((effect as LoginScreenEffects.ShowError).message).isEqualTo(errorMessage)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `initial state has correct default values`() = runTest {
        assertThat(viewModel.state.value.username).isEmpty()
        assertThat(viewModel.state.value.password).isEmpty()
        assertThat(viewModel.state.value.usernameError).isNull()
        assertThat(viewModel.state.value.passwordError).isNull()
        assertThat(viewModel.state.value.isPasswordVisible).isFalse()
        assertThat(viewModel.state.value.isLoading).isFalse()
        assertThat(viewModel.state.value.canSubmit).isFalse()
    }



    @Test
    fun `canSubmit is true after fixing invalid input`() = runTest {
        viewModel.onUsernameChanged("")
        viewModel.onPasswordChanged("")
        viewModel.onLoginClicked()

        viewModel.onUsernameChanged("username")
        viewModel.onPasswordChanged("password")

        assertThat(viewModel.state.value.canSubmit).isTrue()
    }

    @Test
    fun `onLoginClicked allows retry after failure`() = runTest {
        val username = "test-user"
        val password = "wrong-pass"
        viewModel.onUsernameChanged(username)
        viewModel.onPasswordChanged(password)

        val errorMessage = "Invalid credentials"
        coEvery { loginUseCase.login(username, password) } throws InvalidUserOrPasswordException()
        coEvery { identityStringProvider.invalidUserNameAndPasswordError } returns errorMessage

        viewModel.onLoginClicked()
        viewModel.effect.test {
            val effect = awaitItem()
            assertThat(effect).isInstanceOf(LoginScreenEffects.ShowError::class.java)
            cancelAndIgnoreRemainingEvents()
        }

        coEvery { loginUseCase.login(username, password) } returns Unit
        viewModel.onLoginClicked()

        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(LoginScreenEffects.ReturnLoggedInResultCode)
            cancelAndIgnoreRemainingEvents()
        }
    }

}
