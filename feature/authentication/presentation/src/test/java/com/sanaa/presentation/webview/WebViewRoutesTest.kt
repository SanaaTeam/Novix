package com.sanaa.presentation.webview

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class WebViewRoutesTest {

    @Test
    fun `SignUpWebViewRoute implements Destination interface`() {
        // Given
        val route = SignUpWebViewRoute("https://signup.com")

        // Then
        assertThat(route).isInstanceOf(Destination::class.java)
    }

    @Test
    fun `ForgetPasswordWebViewRoute implements Destination interface`() {
        // Given
        val route = ForgetPasswordWebViewRoute("https://reset.com")

        // Then
        assertThat(route).isInstanceOf(Destination::class.java)
    }

    @Test
    fun `SignUpWebViewRoute route() returns correct format`() {
        // Given
        val url = "https://signup.example.com"
        val route = SignUpWebViewRoute(url)

        // When
        val result = route.route()

        // Then
        assertThat(result).isEqualTo("signup?url=$url")
    }

    @Test
    fun `ForgetPasswordWebViewRoute route() returns correct format`() {
        // Given
        val url = "https://reset.example.com"
        val route = ForgetPasswordWebViewRoute(url)

        // When
        val result = route.route()

        // Then
        assertThat(result).isEqualTo("forget_password?url=$url")
    }

    @Test
    fun `SignUpWebViewRoute with empty URL`() {
        // Given
        val route = SignUpWebViewRoute("")

        // When
        val result = route.route()

        // Then
        assertThat(result).isEqualTo("signup?url=")
    }

    @Test
    fun `ForgetPasswordWebViewRoute with empty URL`() {
        // Given
        val route = ForgetPasswordWebViewRoute("")

        // When
        val result = route.route()

        // Then
        assertThat(result).isEqualTo("forget_password?url=")
    }

    @Test
    fun `SignUpWebViewRoute with special characters in URL`() {
        // Given
        val url = "https://signup.com?param=value&other=123"
        val route = SignUpWebViewRoute(url)

        // When
        val result = route.route()

        // Then
        assertThat(result).isEqualTo("signup?url=$url")
    }

    @Test
    fun `ForgetPasswordWebViewRoute with special characters in URL`() {
        // Given
        val url = "https://reset.com?param=value&other=123"
        val route = ForgetPasswordWebViewRoute(url)

        // When
        val result = route.route()

        // Then
        assertThat(result).isEqualTo("forget_password?url=$url")
    }

    @Test
    fun `SignUpWebViewRoute with HTTP URL`() {
        // Given
        val url = "http://signup.com"
        val route = SignUpWebViewRoute(url)

        // When
        val result = route.route()

        // Then
        assertThat(result).isEqualTo("signup?url=$url")
    }

    @Test
    fun `ForgetPasswordWebViewRoute with HTTP URL`() {
        // Given
        val url = "http://reset.com"
        val route = ForgetPasswordWebViewRoute(url)

        // When
        val result = route.route()

        // Then
        assertThat(result).isEqualTo("forget_password?url=$url")
    }

    @Test
    fun `SignUpWebViewRoute with HTTPS URL`() {
        // Given
        val url = "https://secure.signup.com"
        val route = SignUpWebViewRoute(url)

        // When
        val result = route.route()

        // Then
        assertThat(result).isEqualTo("signup?url=$url")
    }

    @Test
    fun `ForgetPasswordWebViewRoute with HTTPS URL`() {
        // Given
        val url = "https://secure.reset.com"
        val route = ForgetPasswordWebViewRoute(url)

        // When
        val result = route.route()

        // Then
        assertThat(result).isEqualTo("forget_password?url=$url")
    }

    @Test
    fun `SignUpWebViewRoute data class properties work correctly`() {
        // Given
        val url = "https://signup.com"
        val route = SignUpWebViewRoute(url)

        // Then
        assertThat(route.url).isEqualTo(url)
    }

    @Test
    fun `ForgetPasswordWebViewRoute data class properties work correctly`() {
        // Given
        val url = "https://reset.com"
        val route = ForgetPasswordWebViewRoute(url)

        // Then
        assertThat(route.url).isEqualTo(url)
    }

    @Test
    fun `SignUpWebViewRoute equals and hashCode work correctly`() {
        // Given
        val url = "https://signup.com"
        val route1 = SignUpWebViewRoute(url)
        val route2 = SignUpWebViewRoute(url)
        val route3 = SignUpWebViewRoute("different")

        // Then
        assertThat(route1).isEqualTo(route2)
        assertThat(route1).isNotEqualTo(route3)
        assertThat(route1.hashCode()).isEqualTo(route2.hashCode())
    }

    @Test
    fun `ForgetPasswordWebViewRoute equals and hashCode work correctly`() {
        // Given
        val url = "https://reset.com"
        val route1 = ForgetPasswordWebViewRoute(url)
        val route2 = ForgetPasswordWebViewRoute(url)
        val route3 = ForgetPasswordWebViewRoute("different")

        // Then
        assertThat(route1).isEqualTo(route2)
        assertThat(route1).isNotEqualTo(route3)
        assertThat(route1.hashCode()).isEqualTo(route2.hashCode())
    }

    @Test
    fun `Destination interface route() method exists`() {
        // Given
        val signUpRoute = SignUpWebViewRoute("https://signup.com")
        val forgetPasswordRoute = ForgetPasswordWebViewRoute("https://reset.com")

        // When
        val signUpResult = signUpRoute.route()
        val forgetPasswordResult = forgetPasswordRoute.route()

        // Then
        assertThat(signUpResult).isNotEmpty()
        assertThat(forgetPasswordResult).isNotEmpty()
    }
} 