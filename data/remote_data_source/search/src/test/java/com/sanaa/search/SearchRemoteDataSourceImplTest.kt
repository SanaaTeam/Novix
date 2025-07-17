package com.sanaa.search

import com.example.env_config.service.LanguageProvider
import com.sanaa.search.dataSource.remote.dto.ActorSearchDto
import com.sanaa.search.dataSource.remote.dto.MovieSearchDto
import com.sanaa.search.dataSource.remote.dto.TvShowSearchDto
import com.sanaa.search.dataSource.remote.response.SearchResponse
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import kotlin.test.assertEquals

class SearchRemoteDataSourceImplTest {

    private lateinit var client: HttpClient
    private lateinit var dataSource: SearchRemoteDataSourceImpl
    private lateinit var languageProvider: LanguageProvider
    private val baseUrl = "https://api.themoviedb.org/3"
    private val apiKey = BuildConfig.TMDB_API_KEY
    private lateinit var mockEngine: MockEngine
    private val logger = LoggerFactory.getLogger(SearchRemoteDataSourceImplTest::class.java)

    @BeforeEach
    fun setUp() {
        mockEngine = MockEngine { request ->
            logger.info("Request URL: ${request.url}")
            logger.info("Request Path: ${request.url.toString().substringBefore("?")}")
            val queryParams = request.url.parameters
            val query = queryParams["query"]
            val page = queryParams["page"]
            val language = queryParams["language"]
            val apiKeyParam = queryParams["api_key"]
            val fullPath = request.url.toString().substringBefore("?")

            if (query != null && page == "1" && language == "en" && apiKeyParam == apiKey) {
                when (fullPath) {
                    "$baseUrl/search/person" -> respond(
                        content = """{
                            "page": 1,
                            "results": [{"id": 1, "name": "Tom Hanks", "profile_path": "/path", "gender": 2}],
                            "total_pages": 1,
                            "total_results": 1
                        }""",
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    )
                    "$baseUrl/search/tv" -> respond(
                        content = """{
                            "page": 1,
                            "results": [{"id": 1, "name": "Breaking Bad", "poster_path": "/path", "first_air_date": null, "vote_average": null, "genre_ids": null}],
                            "total_pages": 1,
                            "total_results": 1
                        }""",
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    )
                    "$baseUrl/search/movie" -> respond(
                        content = """{
                            "page": 1,
                            "results": [{"id": 1, "title": "Inception", "poster_path": "/path", "release_date": null, "vote_average": null, "genre_ids": null}],
                            "total_pages": 1,
                            "total_results": 1
                        }""",
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    )
                    else -> {
                        logger.error("Unexpected path: $fullPath")
                        respondError(HttpStatusCode.NotFound)
                    }
                }
            } else {
                logger.error("Invalid parameters: query=$query, page=$page, language=$language, apiKey=$apiKeyParam")
                respondError(HttpStatusCode.BadRequest)
            }
        }

        client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                    coerceInputValues = true
                })
            }
            expectSuccess = false
        }

        languageProvider = mockk()

        dataSource = SearchRemoteDataSourceImpl(client, baseUrl, languageProvider)
    }

    @Test
    fun `should make GET request with correct parameters and return SearchResponse when valid actor query`() = runTest {
        // Given
        val query = "Tom Hanks"
        val expectedResponse = SearchResponse<ActorSearchDto>(
            page = 1,
            results = listOf(
                ActorSearchDto(
                    id = 1,
                    name = "Tom Hanks",
                    profileImagePath = "/path",
                )
            ),
            totalPages = 1,
            totalResults = 1
        )

        every { languageProvider.getCurrentLanguage() } returns "en"

        // When
        val result = dataSource.searchActors(query)

        // Then
        assertEquals(expectedResponse, result)
        val request = mockEngine.requestHistory.first()
        assertEquals("$baseUrl/search/person", request.url.toString().substringBefore("?"))
        assertEquals(query, request.url.parameters["query"])
        assertEquals("1", request.url.parameters["page"])
        assertEquals("en", request.url.parameters["language"])
        assertEquals(apiKey, request.url.parameters["api_key"])
    }

    @Test
    fun `should make GET request with correct parameters and return SearchResponse when valid tv query`() = runTest {
        // Given
        val query = "Breaking Bad"
        val expectedResponse = SearchResponse<TvShowSearchDto>(
            page = 1,
            results = listOf(
                TvShowSearchDto(
                    id = 1,
                    name = "Breaking Bad",
                    posterImagePath = "/path",
                    releaseDate = null,
                    voteAverage = null,
                    genreIds = null
                )
            ),
            totalPages = 1,
            totalResults = 1
        )
        every { languageProvider.getCurrentLanguage() } returns "en"

        // When
        val result = dataSource.searchTvShows(query)

        // Then
        assertEquals(expectedResponse, result)
        val request = mockEngine.requestHistory.first()
        assertEquals("$baseUrl/search/tv", request.url.toString().substringBefore("?"))
        assertEquals(query, request.url.parameters["query"])
        assertEquals("1", request.url.parameters["page"])
        assertEquals("en", request.url.parameters["language"])
        assertEquals(apiKey, request.url.parameters["api_key"])
    }

    @Test
    fun `should make GET request with correct parameters and return SearchResponse when valid movie query`() = runTest {
        // Given
        val query = "Inception"
        val expectedResponse = SearchResponse<MovieSearchDto>(
            page = 1,
            results = listOf(
                MovieSearchDto(
                    id = 1,
                    title = "Inception",
                    posterImagePath = "/path",
                    releaseDate = null,
                    voteAverage = null,
                    genreIds = null
                )
            ),
            totalPages = 1,
            totalResults = 1
        )
        every { languageProvider.getCurrentLanguage() } returns "en"

        // When
        val result = dataSource.searchMovies(query)

        // Then
        assertEquals(expectedResponse, result)
        val request = mockEngine.requestHistory.first()
        assertEquals("$baseUrl/search/movie", request.url.toString().substringBefore("?"))
        assertEquals(query, request.url.parameters["query"])
        assertEquals("1", request.url.parameters["page"])
        assertEquals("en", request.url.parameters["language"])
        assertEquals(apiKey, request.url.parameters["api_key"])
    }
}