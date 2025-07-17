package com.sanaa.actors

import com.example.preferences.service.LanguageProvider
import com.sanaa.actors.dataSource.remote.ActorRemoteDataSource
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
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ActorRemoteDataSourceImplTest {

    private lateinit var client: HttpClient
    private lateinit var dataSource: ActorRemoteDataSource
    private lateinit var languageProvider: LanguageProvider
    private lateinit var mockEngine: MockEngine

    private val baseUrl = "https://api.themoviedb.org/3"
    private val apiKey = "fake_api_key"

    @BeforeEach
    fun setup() {
        languageProvider = mockk()
        every { languageProvider.getCurrentLanguage() } returns "en"

        mockEngine = MockEngine { request ->
            val url = request.url.toString().substringBefore("?")

            when {
                url.endsWith("/person/1") -> respondJson(
                    """{
                    "id": 1,
                    "name": "Tom Hanks",
                    "profile_path": "/img.jpg",
                    "gender": 2
                }"""
                )

                url.endsWith("/person/1/images") -> respondJson(
                    """{
                    "id": 1,
                    "profiles": [
                        {"aspect_ratio": 1.0, "height": 1000, "width": 800, "file_path": "/img1.jpg", "vote_average": 5.0, "vote_count": 10}
                    ]
                }"""
                )

                url.endsWith("/person/1/movie_credits") -> respondJson(
                    """{
                    "id": 1,
                    "cast": [
                        {"id": 100, "title": "Inception", "vote_average": 8.8}
                    ]
                }"""
                )

                url.endsWith("/person/1/tv_credits") -> respondJson(
                    """{
                    "id": 1,
                    "cast": [
                        {"id": 200, "name": "Breaking Bad", "vote_average": 9.5}
                    ]
                }"""
                )

                else -> respondError(HttpStatusCode.NotFound)
            }
        }

        client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            expectSuccess = false
        }

        dataSource = ActorRemoteDataSourceImpl(client, baseUrl, languageProvider)
    }

    @Test
    fun `getActorDetails returns expected ActorDto`() = runTest {
        val result = dataSource.getActorDetails(1)
        assertEquals(1, result.id)
        assertEquals("Tom Hanks", result.name)
        assertEquals("/img.jpg", result.profileImagePath)
    }

    @Test
    fun `getActorImages returns expected ActorImagesDto`() = runTest {
        val result = dataSource.getActorImages(1)
        assertEquals(1, result.id)
        assertEquals(1, result.profiles.size)
        assertEquals("/img1.jpg", result.profiles[0].path)
    }

    @Test
    fun `getActorTopMovies returns expected ActorMovieCastDto`() = runTest {
        val result = dataSource.getActorTopMovies(1)
        assertEquals(1, result.actorId)
        assertEquals(1, result.cast.size)
        assertEquals("Inception", result.cast[0].title)
    }

    @Test
    fun `getActorTopTvSeries returns expected ActorTvCastDto`() = runTest {
        val result = dataSource.getActorTopTvSeries(1)
        assertEquals(1, result.actorId)
        assertEquals(1, result.cast.size)
        assertEquals("Breaking Bad", result.cast[0].name)
    }

    // Must be an extension on MockRequestHandleScope and return HttpResponseData
    private fun MockRequestHandleScope.respondJson(content: String) = respond(
        content = ByteReadChannel(content),
        status = HttpStatusCode.OK,
        headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
    )

}
