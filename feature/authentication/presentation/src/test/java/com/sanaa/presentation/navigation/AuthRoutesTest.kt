package com.sanaa.presentation.navigation

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class AuthRoutesTest {

    @Test
    fun `all routes extend DestinationLogin`() {
        val routes = listOf(
            WelcomeRoute(),
            LoginRoute,
            SignUpRoute,
            ForgetPasswordRoute,
            HomeScreenRoute
        )

        routes.forEach { route ->
            assertThat(route).isInstanceOf(DestinationLogin::class.java)
        }
    }

    @Test
    fun `singleton routes should return same instance`() {
        assertThat(LoginRoute).isSameInstanceAs(LoginRoute)
        assertThat(SignUpRoute).isSameInstanceAs(SignUpRoute)
        assertThat(ForgetPasswordRoute).isSameInstanceAs(ForgetPasswordRoute)
        assertThat(HomeScreenRoute).isSameInstanceAs(HomeScreenRoute)
    }

    @Test
    fun `WelcomeRoute should create new instance every time`() {
        assertThat(WelcomeRoute()).isNotEqualTo(WelcomeRoute())
    }
}
