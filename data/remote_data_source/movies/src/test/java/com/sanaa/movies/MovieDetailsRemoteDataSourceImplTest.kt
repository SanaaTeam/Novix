package com.sanaa.movies

import com.example.env_config.service.LanguageProvider
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

        engine = MockEngine { request ->
            val url = request.url.toString().substringBefore("?")
            when {
                url.endsWith("/movie/1") -> respondJson(
                    """{"id":1,"poster_path":"/p.jpg","title":"A","genres":[{"id":28,"name":"Action"}],
                       "vote_average":7.0,"runtime":100,"release_date":"2020-01-01","overview":"Ok"}"""
                )
                url.endsWith("/movie/1/images") -> respondJson(
                    """{"id":1,"posters":[{"file_path":"/img.jpg"}],"backdrops":[],"logos":[]}"""
                )
                url.endsWith("/movie/1/credits") -> respondJson(
                    """{"id":1,"cast":[{"id":10,"name":"Actor","character":"Hero","profile_path":"/a.jpg"}],
                       "crew":[]}"""
                )
                url.endsWith("/movie/1/similar") -> respondJson(
                    """{"page":1,"results":[{"id":2,"poster_path":"/p2.jpg","title":"B",
                        "genre_ids":[28],"vote_average":6.5,"release_date":"2021-01-01","overview":"Ok"}],
                       "total_pages":1,"total_results":1}"""
                )
                url.endsWith("/movie/1/reviews") -> respondJson(
                    """{"id":1,"results":[{"id": "rev123","author_details":{"name":"John","username":"j",
                        "avatar_path":"/av.jpg","rating":5.0},"content":"Fine","created_at":"2022-01-01"}],
                      "page":1,"total_pages":1,"total_results":1}"""
                )
                else -> respondError(HttpStatusCode.NotFound)
            }
        }

        client = HttpClient(engine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            expectSuccess = false
        }

        dataSource = MovieDetailsRemoteDataSourceImpl(client, baseUrl, languageProvider)
    }

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
    fun `should return correct movie id in similar movies when fetching similar movies`() = runTest {
        val dto = dataSource.fetchSimilarMoviesByMovieId(1)
        assertEquals(2, dto.results.first().id)
    }

    @Test
    fun `should return correct id when fetching movie reviews`() = runTest {
        val dto = dataSource.fetchReviewsByMovieId(1)
        assertEquals(1, dto.id)
    }

    @Test
    fun `should return correct number of results when fetching movie reviews`() = runTest {
        val dto = dataSource.fetchReviewsByMovieId(1)
        assertEquals(1, dto.results.size)
    }

    @Test
    fun `should return correct review content when fetching movie reviews`() = runTest {
        val dto = dataSource.fetchReviewsByMovieId(1)
        assertEquals("Fine", dto.results.first().content)
    }

    private fun MockRequestHandleScope.respondJson(content: String) = respond(
        ByteReadChannel(content),
        HttpStatusCode.OK,
        headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
    )
}