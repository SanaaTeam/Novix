package com.sanaa.presentation.webview

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sanaa.designsystem.design_system.theme.NovixTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AuthWebViewTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `AuthWebView renders without crashing`() {
        // Given
        val testUrl = "https://test.com"

        // When & Then
        composeTestRule.setContent {
            NovixTheme {
                AuthWebView(webPageUrl = testUrl)
            }
        }

        // Just ensure no crash by checking root node exists
        composeTestRule.onRoot().assertExists()
    }

    @Test
    fun `AuthWebView handles different URLs without crash`() {
        val urls = listOf(
            "https://example.com",
            "https://test.org",
            "http://sample.net",
            "https://auth.example.com"
        )

        urls.forEach { url ->
            composeTestRule.setContent {
                NovixTheme {
                    AuthWebView(webPageUrl = url)
                }
            }
            composeTestRule.onRoot().assertExists()
        }
    }

    @Test
    fun `AuthWebView handles empty URL gracefully`() {
        val emptyUrl = ""

        composeTestRule.setContent {
            NovixTheme {
                AuthWebView(webPageUrl = emptyUrl)
            }
        }

        composeTestRule.onRoot().assertExists()
    }

    @Test
    fun `AuthWebView handles malformed URL gracefully`() {
        val malformedUrl = "not-a-valid-url"

        composeTestRule.setContent {
            NovixTheme {
                AuthWebView(webPageUrl = malformedUrl)
            }
        }

        composeTestRule.onRoot().assertExists()
    }
}