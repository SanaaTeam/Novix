package com.sanaa.presentation.navigation

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class AuthRoutesTest {

    @Test
    fun `WelcomeRoute returns correct route string`() {
        // When
        val route = WelcomeRoute.route()

        // Then
        assertThat(route).isEqualTo("welcome")
    }

    @Test
    fun `WelcomeRoute has correct pattern constant`() {
        // Then
        assertThat(WelcomeRoute.PATTERN).isEqualTo("welcome")
    }

    @Test
    fun `LoginRoute returns correct route string`() {
        // When
        val route = LoginRoute.route()

        // Then
        assertThat(route).isEqualTo("login")
    }

    @Test
    fun `LoginRoute has correct pattern constant`() {
        // Then
        assertThat(LoginRoute.PATTERN).isEqualTo("login")
    }

    @Test
    fun `WelcomeRoute implements DestinationLogin interface`() {
        // Given
        val welcomeRoute: DestinationLogin = WelcomeRoute

        // When
        val route = welcomeRoute.route()

        // Then
        assertThat(route).isEqualTo("welcome")
    }

    @Test
    fun `LoginRoute implements DestinationLogin interface`() {
        // Given
        val loginRoute: DestinationLogin = LoginRoute

        // When
        val route = loginRoute.route()

        // Then
        assertThat(route).isEqualTo("login")
    }

    @Test
    fun `WelcomeRoute is serializable`() {
        // Given
        val welcomeRoute = WelcomeRoute

        // When & Then (should not throw)
        assertThat(welcomeRoute).isInstanceOf(WelcomeRoute::class.java)
    }

    @Test
    fun `LoginRoute is serializable`() {
        // Given
        val loginRoute = LoginRoute

        // When & Then (should not throw)
        assertThat(loginRoute).isInstanceOf(LoginRoute::class.java)
    }
} 