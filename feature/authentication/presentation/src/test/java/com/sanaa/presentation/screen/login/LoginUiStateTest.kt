package com.sanaa.presentation.screen.login

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class LoginUiStateTest {

    @Test
    fun `default LoginUiState has correct default values`() {
        // When
        val state = LoginUiState()

        // Then
        assertThat(state.username).isEmpty()
        assertThat(state.password).isEmpty()
        assertThat(state.usernameError).isNull()
        assertThat(state.passwordError).isNull()
        assertThat(state.isPasswordVisible).isFalse()
        assertThat(state.isLoading).isFalse()
        assertThat(state.canSubmit).isFalse()
    }

    @Test
    fun `LoginUiState with custom values has correct values`() {
        // Given
        val username = "testuser"
        val password = "testpass"
        val usernameError = "Username error"
        val passwordError = "Password error"

        // When
        val state = LoginUiState(
            username = username,
            password = password,
            usernameError = usernameError,
            passwordError = passwordError,
            isPasswordVisible = true,
            isLoading = true,
            canSubmit = true
        )

        // Then
        assertThat(state.username).isEqualTo(username)
        assertThat(state.password).isEqualTo(password)
        assertThat(state.usernameError).isEqualTo(usernameError)
        assertThat(state.passwordError).isEqualTo(passwordError)
        assertThat(state.isPasswordVisible).isTrue()
        assertThat(state.isLoading).isTrue()
        assertThat(state.canSubmit).isTrue()
    }

    @Test
    fun `LoginUiState copy method works correctly`() {
        // Given
        val originalState = LoginUiState(
            username = "original",
            password = "original",
            isPasswordVisible = false,
            isLoading = false,
            canSubmit = false
        )

        // When
        val copiedState = originalState.copy(
            username = "updated",
            isPasswordVisible = true,
            canSubmit = true
        )

        // Then
        assertThat(copiedState.username).isEqualTo("updated")
        assertThat(copiedState.password).isEqualTo("original")
        assertThat(copiedState.usernameError).isNull()
        assertThat(copiedState.passwordError).isNull()
        assertThat(copiedState.isPasswordVisible).isTrue()
        assertThat(copiedState.isLoading).isFalse()
        assertThat(copiedState.canSubmit).isTrue()
    }

    @Test
    fun `LoginUiState equals and hashCode work correctly`() {
        // Given
        val state1 = LoginUiState(
            username = "user",
            password = "pass",
            isPasswordVisible = true,
            isLoading = false,
            canSubmit = true
        )
        val state2 = LoginUiState(
            username = "user",
            password = "pass",
            isPasswordVisible = true,
            isLoading = false,
            canSubmit = true
        )
        val state3 = LoginUiState(
            username = "different",
            password = "pass",
            isPasswordVisible = true,
            isLoading = false,
            canSubmit = true
        )

        // Then
        assertThat(state1).isEqualTo(state2)
        assertThat(state1).isNotEqualTo(state3)
        assertThat(state1.hashCode()).isEqualTo(state2.hashCode())
        assertThat(state1.hashCode()).isNotEqualTo(state3.hashCode())
    }

    @Test
    fun `LoginUiState toString contains all properties`() {
        // Given
        val state = LoginUiState(
            username = "testuser",
            password = "testpass",
            usernameError = "error",
            isPasswordVisible = true,
            isLoading = true,
            canSubmit = true
        )

        // When
        val toString = state.toString()

        // Then
        assertThat(toString).contains("testuser")
        assertThat(toString).contains("testpass")
        assertThat(toString).contains("error")
        assertThat(toString).contains("true")
        assertThat(toString).contains("LoginUiState")
    }

    @Test
    fun `LoginUiState with null errors has correct values`() {
        // When
        val state = LoginUiState(
            username = "user",
            password = "pass",
            usernameError = null,
            passwordError = null
        )

        // Then
        assertThat(state.usernameError).isNull()
        assertThat(state.passwordError).isNull()
    }

    @Test
    fun `LoginUiState with empty strings has correct values`() {
        // When
        val state = LoginUiState(
            username = "",
            password = ""
        )

        // Then
        assertThat(state.username).isEmpty()
        assertThat(state.password).isEmpty()
    }

    @Test
    fun `LoginUiState component functions work correctly`() {
        // Given
        val state = LoginUiState(
            username = "user",
            password = "pass",
            usernameError = "error1",
            passwordError = "error2",
            isPasswordVisible = true,
            isLoading = true,
            canSubmit = true
        )

        // When
        val (username, password, usernameError, passwordError, isPasswordVisible, isLoading, canSubmit) = state

        // Then
        assertThat(username).isEqualTo("user")
        assertThat(password).isEqualTo("pass")
        assertThat(usernameError).isEqualTo("error1")
        assertThat(passwordError).isEqualTo("error2")
        assertThat(isPasswordVisible).isTrue()
        assertThat(isLoading).isTrue()
        assertThat(canSubmit).isTrue()
    }
} 