package com.sanaa.presentation.webview

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.sanaa.presentation.navigation.LocalNavControllerProvider
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

class SignUpWebViewScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun setContentWithNavController(url: String, navController: NavController) {
        composeTestRule.setContent {
            CompositionLocalProvider(LocalNavControllerProvider provides navController as NavHostController) {
                SignUpWebViewScreen(url = url)
            }
        }
    }

    @Test
    fun screen_displaysCorrectUIElements() {
        val navController = mockk<NavController>(relaxed = true)
        val url = "https://example.com/signup"

        setContentWithNavController(url, navController)

        composeTestRule.onNodeWithText("Sign Up").assertIsDisplayed()
        composeTestRule.onNodeWithTag("auth_webview").assertIsDisplayed()
        composeTestRule.onNode(hasContentDescription("icon_cancel")).assertExists()
    }

    @Test
    fun clickingCancelIcon_callsPopBackStack() {
        val navController = mockk<NavController>(relaxed = true)
        val url = "https://example.com/signup"

        setContentWithNavController(url, navController)

        composeTestRule.onNode(hasContentDescription("icon_cancel")).performClick()

        verify { navController.popBackStack() }
    }

    @Test
    fun screen_handlesEmptyUrl_gracefully() {
        val navController = mockk<NavController>(relaxed = true)

        setContentWithNavController("", navController)

        composeTestRule.onNodeWithTag("auth_webview").assertIsDisplayed()
    }

    @Test
    fun screen_handlesLongUrl_gracefully() {
        val navController = mockk<NavController>(relaxed = true)
        val longUrl = "https://example.com/signup?" + "param=value&".repeat(50)

        setContentWithNavController(longUrl, navController)

        composeTestRule.onNodeWithTag("auth_webview").assertIsDisplayed()
    }

    @Test
    fun clickingCancelMultipleTimes_callsPopBackStackEachTime() {
        val navController = mockk<NavController>(relaxed = true)
        val url = "https://example.com/signup"

        setContentWithNavController(url, navController)

        repeat(3) {
            composeTestRule.onNode(hasContentDescription("icon_cancel")).performClick()
        }

        verify(exactly = 3) { navController.popBackStack() }
    }
}
