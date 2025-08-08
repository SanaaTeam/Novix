package com.sanaa.identity.repository

import com.sanaa.identity.dataSoruce.local.dataStore.LocalUserPreferenceDataSource
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.ContentRestriction
import repository.Language
import repository.Theme

class UserPreferencesRepositoryImplTest {

    private lateinit var repository: UserPreferencesRepositoryImpl
    private val localDataSource: LocalUserPreferenceDataSource = mockk()

    @BeforeEach
    fun setUp() {
        repository = UserPreferencesRepositoryImpl(localDataSource)
    }

    @Test
    fun `getLanguage should map codes from localDataSource to Language enum`() = runTest {
        val langCode = "en"
        every { localDataSource.getLanguage() } returns flowOf(langCode)

        val flow = repository.getLanguage()

        flow.collect { value ->
            assert(value == Language.entries.first { it.code == langCode })
        }
        verify { localDataSource.getLanguage() }
    }

    @Test
    fun `getContentRestriction should map strings from localDataSource to ContentRestriction enum`() =
        runTest {
            val restrictionName = ContentRestriction.UNRESTRICTED.name
            every { localDataSource.getContentRestriction() } returns flowOf(restrictionName)

            val flow = repository.getContentRestriction()

            flow.collect { value ->
                assert(value == ContentRestriction.valueOf(restrictionName))
            }
            verify { localDataSource.getContentRestriction() }
        }

    @Test
    fun `getTheme should map strings from localDataSource to Theme enum`() = runTest {
        val themeName = Theme.DARK.name
        every { localDataSource.getTheme() } returns flowOf(themeName)

        val flow = repository.getTheme()

        flow.collect { value ->
            assert(value == Theme.valueOf(themeName))
        }
        verify { localDataSource.getTheme() }
    }

    @Test
    fun `setTheme should pass theme name to localDataSource`() = runTest {
        val theme = Theme.LIGHT
        coEvery { localDataSource.setTheme(any()) } just Runs

        repository.setTheme(theme)

        coVerify { localDataSource.setTheme(theme.name) }
    }

    @Test
    fun `setContentRestriction should pass contentRestriction name to localDataSource`() = runTest {
        val restriction = ContentRestriction.RESTRICTED
        coEvery { localDataSource.setContentRestriction(any()) } just Runs

        repository.setContentRestriction(restriction)

        coVerify { localDataSource.setContentRestriction(restriction.name) }
    }

    @Test
    fun `setLanguage should pass language code to localDataSource`() = runTest {
        val language = Language.ENGLISH
        coEvery { localDataSource.setLanguage(any()) } just Runs

        repository.setLanguage(language)

        coVerify { localDataSource.setLanguage(language.code) }
    }
}