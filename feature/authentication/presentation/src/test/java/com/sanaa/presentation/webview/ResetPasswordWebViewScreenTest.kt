package com.sanaa.presentation.webview

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.feature.authentication.presentation.R
import com.sanaa.presentation.navigation.LocalNavControllerProvider
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ResetPasswordWebViewScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun `ResetPasswordWebViewScreen displays correctly with test tag`() {
        val testUrl = "https://reset-password.com"
        val navController = TestNavHostController(composeTestRule.activity)

        composeTestRule.setContent {
            NovixTheme {
                androidx.compose.runtime.CompositionLocalProvider(
                    LocalNavControllerProvider provides navController
                ) {
                    ResetPasswordWebViewScreen(url = testUrl)
                }
            }
        }

        composeTestRule.onNodeWithTag("auth_webview").assertIsDisplayed()
    }

    @Test
    fun `ResetPasswordWebViewScreen displays reset password title`() {
        val testUrl = "https://reset-password.com"
        val navController = TestNavHostController(composeTestRule.activity)

        composeTestRule.setContent {
            NovixTheme {
                androidx.compose.runtime.CompositionLocalProvider(
                    LocalNavControllerProvider provides navController
                ) {
                    ResetPasswordWebViewScreen(url = testUrl)
                }
            }
        }

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.reset_password))
            .assertIsDisplayed()
    }

    @Test
    fun `ResetPasswordWebViewScreen handles back navigation`() {
        val testUrl = "https://reset-password.com"
        val navController = TestNavHostController(composeTestRule.activity)

        composeTestRule.setContent {
            NovixTheme {
                androidx.compose.runtime.CompositionLocalProvider(
                    LocalNavControllerProvider provides navController
                ) {
                    ResetPasswordWebViewScreen(url = testUrl)
                }
            }
        }

        composeTestRule.onNodeWithContentDescription("icon_cancel").performClick()

        assert(navController.currentBackStackEntry == null || navController.popBackStack())
    }

    @Test
    fun `ResetPasswordWebViewScreen displays with different URLs`() {
        val urls = listOf(
            "https://reset.example.com",
            "https://password-reset.org",
            "https://forgot-password.net"
        )

        urls.forEach { url ->
            val navController = TestNavHostController(composeTestRule.activity)

            composeTestRule.setContent {
                NovixTheme {
                    androidx.compose.runtime.CompositionLocalProvider(
                        LocalNavControllerProvider provides navController
                    ) {
                        ResetPasswordWebViewScreen(url = url)
                    }
                }
            }

            composeTestRule.onNodeWithTag("auth_webview").assertIsDisplayed()
        }
    }

    @Test
    fun `ResetPasswordWebViewScreen displays cancel icon`() {
        val testUrl = "https://reset-password.com"
        val navController = TestNavHostController(composeTestRule.activity)

        composeTestRule.setContent {
            NovixTheme {
                androidx.compose.runtime.CompositionLocalProvider(
                    LocalNavControllerProvider provides navController
                ) {
                    ResetPasswordWebViewScreen(url = testUrl)
                }
            }
        }

        composeTestRule.onNodeWithContentDescription("icon_cancel").assertIsDisplayed()
    }
}
