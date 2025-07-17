package com.sanaa.search

import com.example.env_config.service.LanguageProvider

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SearchRemoteDataSourceImplPaginationTest {

    private val baseUrl = "https://api.themoviedb.org/3"
    private val apiKey = BuildConfig.TMDB_API_KEY
    private val languageProvider: LanguageProvider = mockk()

    private fun buildClientWithMockEngine(page: Int, totalPages: Int, totalResults: Int, movieTitle: String? = null): HttpClient {
        val content = buildString {
            append("""
                {
                    "page": $page,
                    "results": [
            """.trimIndent())

            if (movieTitle != null) {
                append("""
                    {
                        "id": 1,
                        "title": "$movieTitle",
                        "poster_path": "/path/to/image",
                        "release_date": "2023-01-01",
                        "genre_ids": [28, 18],
                        "vote_average": 7.8
                    }
                """.trimIndent())
            }

            append("],")
            append("""
                    "total_pages": $totalPages,
                    "total_results": $totalResults
                }
            """.trimIndent())
        }

        val mockEngine = MockEngine { request ->
            val pageParam = request.url.parameters["page"]
            val query = request.url.parameters["query"]
            val apiKeyParam = request.url.parameters["api_key"]

            if (query != null && pageParam == page.toString() && apiKeyParam == apiKey) {
                respond(
                    content = content,
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                )
            } else {
                respond(
                    content = """{"status_message":"Invalid request"}""",
                    status = HttpStatusCode.BadRequest,
                    headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                )
            }
        }

        return HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                    coerceInputValues = true
                })
            }
        }
    }

    @Test
    fun searchMovies_shouldReturnCorrectPage_whenValidPageRequested() = runTest {
        // Given
        val page = 3
        val totalPages = 10
        val totalResults = 100
        val query = "Batman"
        val movieTitle = "Batman Begins"
        every { languageProvider.getCurrentLanguage() } returns "en"

        val client = buildClientWithMockEngine(page, totalPages, totalResults, movieTitle)
        val dataSource = SearchRemoteDataSourceImpl(client, baseUrl, languageProvider)

        // When
        val result = dataSource.searchMovies(query, page)

        // Then
        assertEquals(page, result.page)
        assertEquals(totalPages, result.totalPages)
        assertEquals(totalResults, result.totalResults)
        assertEquals(1, result.results.size)
        assertEquals(movieTitle, result.results.first().title)
    }

    @Test
    fun searchMovies_shouldHandleEmptyResults_whenPageBeyondLimit() = runTest {
        // Given
        val page = 100
        val totalPages = 10
        val totalResults = 200
        val query = "Test"
        every { languageProvider.getCurrentLanguage() } returns "en"

        val client = buildClientWithMockEngine(page, totalPages, totalResults)
        val dataSource = SearchRemoteDataSourceImpl(client, baseUrl, languageProvider)

        // When
        val result = dataSource.searchMovies(query, page)

        // Then
        assertEquals(page, result.page)
        assertEquals(totalPages, result.totalPages)
        assertTrue(result.results.isEmpty())
    }

    @Test
    fun searchMovies_shouldHandleLastPageCorrectly() = runTest {
        // Given
        val page = 5
        val totalPages = 5
        val totalResults = 100
        val query = "Final"
        every { languageProvider.getCurrentLanguage() } returns "en"

        val client = buildClientWithMockEngine(page, totalPages, totalResults)
        val dataSource = SearchRemoteDataSourceImpl(client, baseUrl, languageProvider)

        // When
        val result = dataSource.searchMovies(query, page)

        // Then
        assertEquals(page, result.page)
        assertEquals(totalPages, result.totalPages)
        assertTrue(result.results.isEmpty())
    }
}
