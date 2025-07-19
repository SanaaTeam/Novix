package com.sanaa.series

import com.sanaa.preferences.service.LanguageProvider
import com.sanaa.series.data_source.remote.RemoteTvSeriesDataSource
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.ByteReadChannel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertEquals

class RemoteTvSeriesDataSourceImplTest {

    private lateinit var mockEngine: MockEngine
    private lateinit var client: HttpClient
    private lateinit var languageProvider: LanguageProvider
    private lateinit var dataSource: RemoteTvSeriesDataSourceImpl
    private val baseUrl = "https://api.themoviedb.org/3"
    private val apiKey = BuildConfig.TMDB_API_KEY

    @BeforeEach
    fun setup() {
        languageProvider = mockk()
        every { languageProvider.getCurrentLanguage() } returns "en"

        mockEngine = MockEngine { request ->
            val url = request.url.toString().substringBefore("?")
            val queryParams = request.url.parameters
            val language = queryParams["language"]
            val apiKeyParam = queryParams["api_key"]

            when {
                url.endsWith("/tv/100") -> respondJson(
                    """{
                        "id": 100,
                        "name": "Stranger Things",
                        "overview": "A show about mysterious events.",
                        "first_air_date": "2016-07-15",
                        "vote_average": 8.7,
                        "poster_path": "/poster.jpg",
                        "number_of_seasons": 4,
                        "genres": [
                            {"id": 18, "name": "Drama"},
                            {"id": 9648, "name": "Mystery"}
                        ]
                    }"""
                )

                url.endsWith("/tv/100/videos") -> respondJson(
                    """{
                        "id": 100,
                        "results": [
                            {"id": "abc123", "key": "xyz", "site": "YouTube", "type": "Trailer"}
                        ]
                    }"""
                )

                url.endsWith("/tv/100/season/1") -> respondJson(
                    """{
                        "id": 100,
                        "name": "Season 1"
                    }"""
                )

                url.endsWith("/tv/100/images") -> respondJson(
                    """{
                        "id": 100,
                        "backdrops": [
                            {"file_path": "/image.jpg"}
                        ]
                    }"""
                )

                url.contains("/discover/tv") -> respondJson(
                    """{
                        "page": 1,
                        "results": [
                            {
                                "id": 123,
                                "name": "Breaking Bad",
                                "overview": "A high school chemistry teacher...",
                                "poster_path": "/somepath.jpg",
                                "vote_average": 9.5
                            }
                        ],
                        "total_pages": 10,
                        "total_results": 200
                    }"""
                )

                url.endsWith("/tv/100/reviews") -> respondJson(
                    """{
                        "id": 100,
                        "page": 1,
                        "results": [
                            {
                                "id": "r1",
                                "author": "Haider",
                                "content": "This show is amazing!",
                                "created_at": "2024-01-01T12:00:00.000Z"
                            }
                        ]
                    }"""
                )

                url.endsWith("/tv/100/credits") -> respondJson(
                    """{
                        "id": 100,
                        "cast": [
                            {
                                "id": 1,
                                "name": "Actor Name",
                                "character": "Main Character",
                                "profile_path": "/actor.jpg"
                            }
                        ]
                    }"""
                )

                url.endsWith("/tv/100/season/1/episode/1") -> respondJson(
                    """{
                        "id": 1,
                        "name": "Pilot"
                    }"""
                )

                url.endsWith("/tv/100/season/1/episode/1/images") -> respondJson(
                    """{
                        "id": 1,
                        "backdrops": [
                            {"file_path": "/image1.jpg"}
                        ]
                    }"""
                )

                url.endsWith("/tv/100/season/1/episode/1/credits") -> respondJson(
                    """{
                        "id": 1,
                        "guest_stars": [
                            {
                                "id": 2,
                                "name": "Guest Star",
                                "profile_path": "/guest.jpg"
                            }
                        ]
                    }"""
                )

                else -> respondError(HttpStatusCode.NotFound)
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
        }

        dataSource = RemoteTvSeriesDataSourceImpl(
            client = client,
            languageProvider = languageProvider
        )
    }

    @Test
    fun `getTvSeries returns expected result`() = runTest {
        val result = dataSource.getTvSeries(100)
        assertEquals("Stranger Things", result.name)
    }

    @Test
    fun `getTvSeriesVideos returns video list`() = runTest {
        val result = dataSource.getTvSeriesVideos(100)
        assertEquals(1, result.size)
        assertEquals("YouTube", result.first().site)
    }

    @Test
    fun `getTvSeriesSeasonDetails returns season info`() = runTest {
        val result = dataSource.getTvSeriesSeasonDetails(100, 1)
        assertEquals("Season 1", result.name)
    }

    @Test
    fun `getTvSeriesImages returns image list`() = runTest {
        val result = dataSource.getTvSeriesImages(100)
        assertEquals("/image.jpg", result.first().filePath)
    }

    @Test
    fun `getTvSeriesByGenre returns list`() = runTest {
        val result = dataSource.getTvSeriesByGenre(18)
        assertEquals(1, result.size)
    }

    @Test
    fun `getTvSeriesReviews returns review list`() = runTest {
        val result = dataSource.getTvSeriesReviews(100)
        assertEquals("This show is amazing!", result.first().content)
    }

    @Test
    fun `getTvSeriesCast returns cast list`() = runTest {
        val result = dataSource.getTvSeriesCast(100)
        assertEquals("Actor Name", result.first().name)
    }

    @Test
    fun `getEpisodeDetails returns episode`() = runTest {
        val result = dataSource.getEpisodeDetails(100, 1, 1)
        assertEquals("Pilot", result.name)
    }

    @Test
    fun `getEpisodeImages returns episode images`() = runTest {
        val result = dataSource.getEpisodeImages(100, 1, 1)
        assertEquals("/image1.jpg", result.first().filePath)
    }

    @Test
    fun `getEpisodeGuestsOfHonor returns guests`() = runTest {
        val result = dataSource.getEpisodeGuestsOfHonor(100, 1, 1)
        assertEquals("Guest Star", result.first().name)
    }

    private fun MockRequestHandleScope.respondJson(content: String) = respond(
        content = ByteReadChannel(content),
        status = HttpStatusCode.OK,
        headers = headersOf(
            HttpHeaders.ContentType,
            listOf(ContentType.Application.Json.toString())
        )
    )
}