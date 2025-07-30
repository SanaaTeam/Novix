package com.sanaa.presentation.api

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.sanaa.api.AuthenticationApi
import com.sanaa.api.HomeFeatureApi
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AuthenticationApiImplTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `AuthenticationApiImpl implements AuthenticationApi interface`() {
        val mockHomeApi = mockk<HomeFeatureApi>()
        val apiImpl = AuthenticationApiImpl(mockHomeApi)
        assertThat(apiImpl).isInstanceOf(AuthenticationApi::class.java)
    }

    @Test
    fun `AuthenticationApiImpl instantiation works`() {
        val mockHomeApi = mockk<HomeFeatureApi>()
        val apiImpl = AuthenticationApiImpl(mockHomeApi)

        assertThat(apiImpl).isNotNull()
        assertThat(apiImpl::class.java.simpleName).isEqualTo("AuthenticationApiImpl")
    }

    @Test
    fun `AuthenticationApiImpl can be cast to AuthenticationApi`() {
        val mockHomeApi = mockk<HomeFeatureApi>()
        val apiImpl = AuthenticationApiImpl(mockHomeApi)
        val castedApi = apiImpl as AuthenticationApi
        assertThat(castedApi).isEqualTo(apiImpl)
    }

    @Test
    fun `AuthenticationApiImpl AuthenticationScreen method exists and matches interface`() {
        val mockHomeApi = mockk<HomeFeatureApi>()
        val apiImpl = AuthenticationApiImpl(mockHomeApi)
        val interfaceMethod = AuthenticationApi::class.java.getMethod("AuthenticationScreen", Context::class.java)
        val implementationMethod = apiImpl::class.java.getMethod("AuthenticationScreen", Context::class.java)

        assertThat(implementationMethod.returnType).isEqualTo(interfaceMethod.returnType)
        assertThat(implementationMethod.parameterTypes).isEqualTo(interfaceMethod.parameterTypes)
    }

    @Test
    fun `AuthenticationScreen composable can be called without crashing`() {
        val mockHomeApi = mockk<HomeFeatureApi>()
        val apiImpl = AuthenticationApiImpl(mockHomeApi)
        val mockContext = mockk<Context>()

        composeTestRule.setContent {
            apiImpl.AuthenticationScreen(mockContext)
        }
    }
}
