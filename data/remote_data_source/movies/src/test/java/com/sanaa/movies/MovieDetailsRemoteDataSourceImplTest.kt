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
                    """{"id":1,"results":[{"author_details":{"name":"John","username":"j",
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

    private fun MockRequestHandleScope.respondJson(content: String) = respond(
        ByteReadChannel(content),
        HttpStatusCode.OK,
        headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
    )

    @Test fun `fetchMovieDetails returns correct DTO`() = runTest {
        val dto = dataSource.fetchMovieDetails(1)
        assertEquals(1, dto.id)
        assertEquals("/p.jpg", dto.posterImagePath)
        assertEquals("A", dto.title)
        assertEquals(100, dto.duration)
    }

    @Test fun `fetchImages returns correct MovieImagesDto`() = runTest {
        val dto = dataSource.fetchImages(1)
        assertEquals(1, dto.id)
        assertEquals(1, dto.posters.size)
        assertEquals("/img.jpg", dto.posters.first().filePath)
    }

    @Test fun `fetchCast returns correct CastDto`() = runTest {
        val dto = dataSource.fetchCast(1)
        assertEquals(1, dto.id)
        assertEquals(1, dto.cast.size)
        assertEquals("Actor", dto.cast.first().name)
    }

    @Test fun `fetchSimilarMoviesByMovieId returns correct SimilarMoviesDto`() = runTest {
        val dto = dataSource.fetchSimilarMoviesByMovieId(1)
        assertEquals(1, dto.page)
        assertEquals(1, dto.results.size)
        assertEquals(2, dto.results.first().id)
    }

    @Test fun `fetchReviewsByMovieId returns correct ReviewDto`() = runTest {
        val dto = dataSource.fetchReviewsByMovieId(1)
        assertEquals(1, dto.id)
        assertEquals(1, dto.results.size)
        assertEquals("Fine", dto.results.first().content)
    }
}