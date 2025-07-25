package com.sanaa.presentation.screen.login

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class LoginScreenEffectsTest {

    @Test
    fun `NavigateBack effect is singleton object`() {
        // When
        val effect1 = LoginScreenEffects.NavigateBack
        val effect2 = LoginScreenEffects.NavigateBack

        // Then
        assertThat(effect1).isSameInstanceAs(effect2)
        assertThat(effect1).isEqualTo(effect2)
        assertThat(effect1.hashCode()).isEqualTo(effect2.hashCode())
    }

    @Test
    fun `NavigateToHome effect is singleton object`() {
        // When
        val effect1 = LoginScreenEffects.NavigateToHome
        val effect2 = LoginScreenEffects.NavigateToHome

        // Then
        assertThat(effect1).isSameInstanceAs(effect2)
        assertThat(effect1).isEqualTo(effect2)
        assertThat(effect1.hashCode()).isEqualTo(effect2.hashCode())
    }

    @Test
    fun `NavigateToForgotPassword effect is singleton object`() {
        // When
        val effect1 = LoginScreenEffects.NavigateToForgotPassword
        val effect2 = LoginScreenEffects.NavigateToForgotPassword

        // Then
        assertThat(effect1).isSameInstanceAs(effect2)
        assertThat(effect1).isEqualTo(effect2)
        assertThat(effect1.hashCode()).isEqualTo(effect2.hashCode())
    }

    @Test
    fun `NavigateToCreateAccount effect is singleton object`() {
        // When
        val effect1 = LoginScreenEffects.NavigateToCreateAccount
        val effect2 = LoginScreenEffects.NavigateToCreateAccount

        // Then
        assertThat(effect1).isSameInstanceAs(effect2)
        assertThat(effect1).isEqualTo(effect2)
        assertThat(effect1.hashCode()).isEqualTo(effect2.hashCode())
    }

    @Test
    fun `ShowError effect with message has correct properties`() {
        // Given
        val errorMessage = "Invalid credentials"

        // When
        val effect = LoginScreenEffects.ShowError(errorMessage)

        // Then
        assertThat(effect.message).isEqualTo(errorMessage)
    }

    @Test
    fun `ShowSuccess effect with message has correct properties`() {
        // Given
        val successMessage = "Login successful"

        // When
        val effect = LoginScreenEffects.ShowSuccess(successMessage)

        // Then
        assertThat(effect.message).isEqualTo(successMessage)
    }

    @Test
    fun `ShowError effects with same message are equal`() {
        // Given
        val message = "Error message"
        val effect1 = LoginScreenEffects.ShowError(message)
        val effect2 = LoginScreenEffects.ShowError(message)

        // Then
        assertThat(effect1).isEqualTo(effect2)
        assertThat(effect1.hashCode()).isEqualTo(effect2.hashCode())
    }

    @Test
    fun `ShowSuccess effects with same message are equal`() {
        // Given
        val message = "Success message"
        val effect1 = LoginScreenEffects.ShowSuccess(message)
        val effect2 = LoginScreenEffects.ShowSuccess(message)

        // Then
        assertThat(effect1).isEqualTo(effect2)
        assertThat(effect1.hashCode()).isEqualTo(effect2.hashCode())
    }

    @Test
    fun `ShowError effects with different messages are not equal`() {
        // Given
        val effect1 = LoginScreenEffects.ShowError("Error 1")
        val effect2 = LoginScreenEffects.ShowError("Error 2")

        // Then
        assertThat(effect1).isNotEqualTo(effect2)
        assertThat(effect1.hashCode()).isNotEqualTo(effect2.hashCode())
    }

    @Test
    fun `ShowSuccess effects with different messages are not equal`() {
        // Given
        val effect1 = LoginScreenEffects.ShowSuccess("Success 1")
        val effect2 = LoginScreenEffects.ShowSuccess("Success 2")

        // Then
        assertThat(effect1).isNotEqualTo(effect2)
        assertThat(effect1.hashCode()).isNotEqualTo(effect2.hashCode())
    }

    @Test
    fun `ShowError toString contains message`() {
        // Given
        val message = "Test error message"
        val effect = LoginScreenEffects.ShowError(message)

        // When
        val toString = effect.toString()

        // Then
        assertThat(toString).contains(message)
        assertThat(toString).contains("ShowError")
    }

    @Test
    fun `ShowSuccess toString contains message`() {
        // Given
        val message = "Test success message"
        val effect = LoginScreenEffects.ShowSuccess(message)

        // When
        val toString = effect.toString()

        // Then
        assertThat(toString).contains(message)
        assertThat(toString).contains("ShowSuccess")
    }

    @Test
    fun `ShowError copy method works correctly`() {
        // Given
        val originalEffect = LoginScreenEffects.ShowError("Original message")

        // When
        val copiedEffect = originalEffect.copy(message = "Updated message")

        // Then
        assertThat(copiedEffect.message).isEqualTo("Updated message")
        assertThat(originalEffect.message).isEqualTo("Original message")
    }

    @Test
    fun `ShowSuccess copy method works correctly`() {
        // Given
        val originalEffect = LoginScreenEffects.ShowSuccess("Original message")

        // When
        val copiedEffect = originalEffect.copy(message = "Updated message")

        // Then
        assertThat(copiedEffect.message).isEqualTo("Updated message")
        assertThat(originalEffect.message).isEqualTo("Original message")
    }

    @Test
    fun `ShowError component function works correctly`() {
        // Given
        val message = "Component test message"
        val effect = LoginScreenEffects.ShowError(message)

        // When
        val (extractedMessage) = effect

        // Then
        assertThat(extractedMessage).isEqualTo(message)
    }

    @Test
    fun `ShowSuccess component function works correctly`() {
        // Given
        val message = "Component test message"
        val effect = LoginScreenEffects.ShowSuccess(message)

        // When
        val (extractedMessage) = effect

        // Then
        assertThat(extractedMessage).isEqualTo(message)
    }

    @Test
    fun `ShowError with empty message works correctly`() {
        // When
        val effect = LoginScreenEffects.ShowError("")

        // Then
        assertThat(effect.message).isEmpty()
    }

    @Test
    fun `ShowSuccess with empty message works correctly`() {
        // When
        val effect = LoginScreenEffects.ShowSuccess("")

        // Then
        assertThat(effect.message).isEmpty()
    }

    @Test
    fun `all effects are instances of LoginScreenEffects`() {
        // Then
        assertThat(LoginScreenEffects.NavigateBack).isInstanceOf(LoginScreenEffects::class.java)
        assertThat(LoginScreenEffects.NavigateToHome).isInstanceOf(LoginScreenEffects::class.java)
        assertThat(LoginScreenEffects.NavigateToForgotPassword).isInstanceOf(LoginScreenEffects::class.java)
        assertThat(LoginScreenEffects.NavigateToCreateAccount).isInstanceOf(LoginScreenEffects::class.java)
        assertThat(LoginScreenEffects.ShowError("test")).isInstanceOf(LoginScreenEffects::class.java)
        assertThat(LoginScreenEffects.ShowSuccess("test")).isInstanceOf(LoginScreenEffects::class.java)
    }

    @Test
    fun `different effect types are not equal`() {
        // Given
        val navigateBack = LoginScreenEffects.NavigateBack
        val navigateToHome = LoginScreenEffects.NavigateToHome
        val showError = LoginScreenEffects.ShowError("test")
        val showSuccess = LoginScreenEffects.ShowSuccess("test")

        // Then
        assertThat(navigateBack).isNotEqualTo(navigateToHome)
        assertThat(navigateBack).isNotEqualTo(showError)
        assertThat(navigateBack).isNotEqualTo(showSuccess)
        assertThat(showError).isNotEqualTo(showSuccess)
    }
} 