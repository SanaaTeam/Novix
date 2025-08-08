package com.sanaa.presentation.webview

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.sanaa.designsystem.design_system.component.navigation.LocalNavControllerProvider
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

class ResetPasswordWebViewScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun setContentWithNavController(url: String, navController: NavController) {
        composeTestRule.setContent {
            CompositionLocalProvider(LocalNavControllerProvider provides navController as NavHostController) {
                ResetPasswordWebViewScreen(url = url)
            }
        }
    }

    @Test
    fun screen_displaysCorrectUIElements() {
        val navController = mockk<NavController>(relaxed = true)
        val url = "https://example.com/reset"

        setContentWithNavController(url, navController)

        composeTestRule.onNodeWithText("Reset Password").assertIsDisplayed()
        composeTestRule.onNodeWithTag("auth_webview").assertIsDisplayed()
        composeTestRule.onNode(hasContentDescription("icon_cancel")).assertExists()
    }

    @Test
    fun clickingTopBarBackButton_callsNavControllerPopBackStack() {
        val navController = mockk<NavController>(relaxed = true)
        val url = "https://example.com/reset"

        setContentWithNavController(url, navController)

        composeTestRule.onNode(hasContentDescription("icon_cancel")).performClick()

        verify { navController.popBackStack() }
    }

    @Test
    fun screen_withEmptyUrl_stillShowsWebView() {
        val navController = mockk<NavController>(relaxed = true)

        setContentWithNavController("", navController)

        composeTestRule.onNodeWithTag("auth_webview").assertIsDisplayed()
    }

    @Test
    fun screen_withVeryLongUrl_showsWebView() {
        val navController = mockk<NavController>(relaxed = true)
        val longUrl = "https://example.com/reset?" + "param=".repeat(100)

        setContentWithNavController(longUrl, navController)

        composeTestRule.onNodeWithTag("auth_webview").assertIsDisplayed()
    }

    @Test
    fun screen_multipleClicksOnBackButton_callsPopBackStackEachTime() {
        val navController = mockk<NavController>(relaxed = true)
        val url = "https://example.com/reset"

        setContentWithNavController(url, navController)

        repeat(3) {
            composeTestRule.onNode(hasContentDescription("icon_cancel")).performClick()
        }

        verify(exactly = 3) { navController.popBackStack() }
    }
}
