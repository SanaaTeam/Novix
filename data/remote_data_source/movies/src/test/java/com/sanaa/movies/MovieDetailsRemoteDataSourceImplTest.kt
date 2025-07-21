package com.sanaa.movies

import com.sanaa.preferences.service.LanguageProvider
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MovieDetailsRemoteDataSourceImplTest {

    private lateinit var client: HttpClient
    private lateinit var dataSource: MovieDetailsRemoteDataSourceImpl
    private lateinit var languageProvider: LanguageProvider
    private lateinit var engine: MockEngine

    private val baseUrl = "https://api.themoviedb.org/3"

    @BeforeEach
    fun setup() {
        languageProvider = mockk()
        every { languageProvider.getCurrentLanguage() } returns "en"

        @Test
        fun `should return correct id when fetching movie details`() = runTest {
            val dto = dataSource.fetchMovieDetails(1)
            assertEquals(1, dto.id)
        }

        @Test
        fun `should return correct posterImagePath when fetching movie details`() = runTest {
            val dto = dataSource.fetchMovieDetails(1)
            assertEquals("/p.jpg", dto.posterImagePath)
        }

        @Test
        fun `should return correct title when fetching movie details`() = runTest {
            val dto = dataSource.fetchMovieDetails(1)
            assertEquals("A", dto.title)
        }

        @Test
        fun `should return correct duration when fetching movie details`() = runTest {
            val dto = dataSource.fetchMovieDetails(1)
            assertEquals(100, dto.duration)
        }

        @Test
        fun `should return correct id when fetching movie images`() = runTest {
            val dto = dataSource.fetchImagesUrl(1)
            assertEquals(1, dto.id)
        }

        @Test
        fun `should return correct number of posters when fetching movie images`() = runTest {
            val dto = dataSource.fetchImagesUrl(1)
            assertEquals(1, dto.posters.size)
        }

        @Test
        fun `should return correct poster path when fetching movie images`() = runTest {
            val dto = dataSource.fetchImagesUrl(1)
            assertEquals("/img.jpg", dto.posters.first().filePath)
        }

        @Test
        fun `should return correct id when fetching movie cast`() = runTest {
            val dto = dataSource.fetchCast(1)
            assertEquals(1, dto.id)
        }

        @Test
        fun `should return correct cast size when fetching movie cast`() = runTest {
            val dto = dataSource.fetchCast(1)
            assertEquals(1, dto.cast.size)
        }

        @Test
        fun `should return correct cast name when fetching movie cast`() = runTest {
            val dto = dataSource.fetchCast(1)
            assertEquals("Actor", dto.cast.first().name)
        }

        @Test
        fun `should return correct page when fetching similar movies`() = runTest {
            val dto = dataSource.fetchSimilarMoviesByMovieId(1)
            assertEquals(1, dto.page)
        }

        @Test
        fun `should return correct number of results when fetching similar movies`() = runTest {
            val dto = dataSource.fetchSimilarMoviesByMovieId(1)
            assertEquals(1, dto.results.size)
        }

        @Test
        fun `should return correct movie id in similar movies when fetching similar movies`() =
            runTest {
                val dto = dataSource.fetchSimilarMoviesByMovieId(1)
                assertEquals(2, dto.results.first().id)
            }

        @Test
        fun `should return correct id when fetching movie reviews`() = runTest {
            val dto = dataSource.fetchReviewsByMovieId(1)
            assertEquals("rev123", dto.first().id)
        }

        @Test
        fun `should return correct number of results when fetching movie reviews`() = runTest {
            val dto = dataSource.fetchReviewsByMovieId(1)
            assertEquals(1, dto.size)
        }

        @Test
        fun `should return correct review content when fetching movie reviews`() = runTest {
            val dto = dataSource.fetchReviewsByMovieId(1)
            assertEquals("Fine", dto.first().content)
        }

    }