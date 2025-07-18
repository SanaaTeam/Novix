package com.sanaa.series

import com.example.env_config.service.LanguageProvider
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
import io.ktor.http.fullPath
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
import kotlin.test.assertTrue

class RemoteTvSeriesDataSourceImplTest {

    private lateinit var dataSource: RemoteTvSeriesDataSource
    private lateinit var httpClient: HttpClient
    private lateinit var languageProvider: LanguageProvider

    private val baseUrl = "https://api.themoviedb.org/3"

    @BeforeEach
    fun setUp() {
        languageProvider = mockk {
            every { getCurrentLanguage() } returns "en"
        }

        val mockEngine = MockEngine { request ->
            val url = request.url.toString().substringBefore("?")

            when {
                url.endsWith("/tv/1") -> respondJson(
                    """{
                        "id": 1,
                        "name": "Breaking Bad",
                        "overview": "A high school chemistry teacher turned methamphetamine manufacturer",
                        "poster_path": "/poster.jpg",
                        "backdrop_path": "/backdrop.jpg",
                        "first_air_date": "2008-01-20",
                        "vote_average": 9.5,
                        "vote_count": 1000,
                        "popularity": 100.0,
                        "genres": [{"id": 18, "name": "Drama"}],
                        "number_of_seasons": 5,
                        "number_of_episodes": 62
                    }"""
                )

                url.endsWith("/tv/1/reviews") -> respondJson(
                    """{
                        "id": 1,
                        "page": 1,
                        "results": [
                            {
                                "author": "Reviewer 1",
                                "author_details": {
                                    "name": "Reviewer 1",
                                    "username": "reviewer1",
                                    "avatar_path": "/path/to/avatar1.jpg",
                                    "rating": 8.5
                                },
                                "content": "Amazing series!",
                                "created_at": "2023-01-01T00:00:00.000Z",
                                "id": "review1",
                                "updated_at": "2023-01-01T00:00:00.000Z",
                                "url": "https://example.com/review1"
                            },
                            {
                                "author": "Reviewer 2",
                                "author_details": {
                                    "name": "Reviewer 2",
                                    "username": "reviewer2",
                                    "avatar_path": "/path/to/avatar2.jpg",
                                    "rating": 9.0
                                },
                                "content": "Excellent series!",
                                "created_at": "2023-01-02T00:00:00.000Z",
                                "id": "review2",
                                "updated_at": "2023-01-02T00:00:00.000Z",
                                "url": "https://example.com/review2"
                            }
                        ],
                        "total_pages": 5,
                        "total_results": 100
                    }"""
                )

                url.endsWith("/tv/1/videos") -> respondJson(
                    """{
                        "id": 1,
                        "results": [
                            {
                                "id": "video1",
                                "key": "abc123",
                                "name": "Trailer",
                                "site": "YouTube",
                                "size": 1080,
                                "type": "Trailer",
                                "official": true,
                                "published_at": "2023-01-01T00:00:00.000Z"
                            }
                        ]
                    }"""
                )

                url.endsWith("/tv/1/credits") -> respondJson(
                    """{
                        "id": 1,
                        "cast": [
                            {
                                "id": 1,
                                "name": "Bryan Cranston",
                                "profile_path": "/actor1.jpg",
                                "character": "Walter White",
                                "order": 0
                            }
                        ]
                    }"""
                )

                url.endsWith("/tv/1/images") -> respondJson(
                    """{
                        "id": 1,
                        "backdrops": [
                            {
                                "aspect_ratio": 1.778,
                                "file_path": "/backdrop1.jpg",
                                "height": 1080,
                                "width": 1920,
                                "iso_639_1": "en",
                                "vote_average": 5.5,
                                "vote_count": 10
                            }
                        ]
                    }"""
                )

                url.endsWith("/tv/1/season/1") -> respondJson(
                    """{
                        "id": 1,
                        "name": "Season 1",
                        "overview": "Season 1 overview",
                        "poster_path": "/season1.jpg",
                        "season_number": 1,
                        "episodes": [
                            {
                                "id": 1,
                                "name": "Pilot",
                                "overview": "Pilot episode",
                                "still_path": "/episode1.jpg",
                                "episode_number": 1,
                                "air_date": "2008-01-20"
                            }
                        ]
                    }"""
                )

                else -> respondError(HttpStatusCode.NotFound)
            }
        }

        httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            expectSuccess = false
        }

        dataSource = RemoteTvSeriesDataSourceImpl(httpClient, baseUrl, languageProvider)
    }

    @Test
    fun `getTvSeriesReviews_shouldReturnReviewsList`() = runTest {
        // When
        val result = dataSource.getTvSeriesReviews(1)

        // Then
        assertEquals(2, result.size)
        assertEquals("Reviewer 1", result[0].author)
        assertEquals("Amazing series!", result[0].content)
        assertEquals("review1", result[0].id)
        assertEquals("Reviewer 2", result[1].author)
        assertEquals("Excellent series!", result[1].content)
        assertEquals("review2", result[1].id)
    }


    @Test
    fun `getTvSeriesCast_shouldReturnCastList_whenValidResponse`() = runTest {
        // Given
        val mockEngine = MockEngine { request ->
            val url = request.url.toString().substringBefore("?")
            when {
                url.endsWith("/tv/123/credits") -> respondJson(
                    """{
                    "id": 123,
                    "cast": [
                        {
                            "id": 1,
                            "name": "John Smith",
                            "profile_path": "/john.jpg",
                            "character": "Hero"
                        },
                        {
                            "id": 2,
                            "name": "Jane Doe",
                            "profile_path": "/jane.jpg",
                            "character": "Villain"
                        }
                    ]
                }"""
                )

                else -> respondError(HttpStatusCode.NotFound)
            }
        }

        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            expectSuccess = false
        }

        val dataSource = RemoteTvSeriesDataSourceImpl(client, baseUrl, languageProvider)

        // When
        val result = dataSource.getTvSeriesCast(123)

        // Then
        assertEquals(2, result.size)
        assertEquals("John Smith", result[0].name)
        assertEquals("Jane Doe", result[1].name)
    }


    @Test
    fun `getEpisodeDetails_shouldReturnEpisodeDetails_whenValidResponse`() = runTest {
        // Given
        val mockEngine = MockEngine { request ->
            val url = request.url.toString().substringBefore("?")
            when {
                url.endsWith("/tv/100/season/1/episode/5") -> respondJson(
                    """{
                    "id": 505,
                    "name": "Episode Title",
                    "overview": "This is a sample episode overview.",
                    "season_number": 1,
                    "episode_number": 5,
                    "air_date": "2024-05-01",
                    "still_path": "/sample.jpg"
                }"""
                )

                else -> respondError(HttpStatusCode.NotFound)
            }
        }

        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            expectSuccess = false
        }

        val dataSource = RemoteTvSeriesDataSourceImpl(client, baseUrl, languageProvider)

        // When
        val result = dataSource.getEpisodeDetails(
            seriesId = 100,
            seasonNumber = 1,
            episodeNumber = 5
        )

        // Then
        assertEquals(505, result.id)
        assertEquals("Episode Title", result.name)
        assertEquals(1, result.seasonNumber)
        assertEquals(5, result.episodeNumber)
    }


    @Test
    fun `getTvSeriesImages_shouldReturnListOfImages_whenValidResponse`() = runTest {
        // Given -
        val mockEngine = MockEngine { request ->
            val url = request.url.toString().substringBefore("?")
            when {
                url.endsWith("/tv/123/images") -> respondJson(
                    """{
                    "id": 123,
                    "backdrops": [
                        {
                            "aspect_ratio": 1.78,
                            "file_path": "/image1.jpg",
                            "height": 1080,
                            "width": 1920
                        },
                        {
                            "aspect_ratio": 1.78,
                            "file_path": "/image2.jpg",
                            "height": 720,
                            "width": 1280
                        }
                    ]
                }"""
                )

                else -> respondError(HttpStatusCode.NotFound)
            }
        }

        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            expectSuccess = false
        }

        val dataSource = RemoteTvSeriesDataSourceImpl(client, baseUrl, languageProvider)

        // When
        val result = dataSource.getTvSeriesImages(123)

        // Then
        assertEquals(2, result.size)
        assertEquals("/image1.jpg", result[0].filePath)
        assertEquals("/image2.jpg", result[1].filePath)
    }

    @Test
    fun `getTvSeriesSeasonDetails_shouldReturnSeasonDto_whenValidResponse`() = runTest {
        // Given - mock response with season details
        val mockEngine = MockEngine { request ->
            val url = request.url.toString().substringBefore("?")
            when {
                url.endsWith("/tv/1/season/2") -> respondJson(
                    """{
                    "id": 1002,
                    "season_number": 2,
                    "name": "Season 2",
                    "overview": "This is season 2",
                    "episodes": [
                        {
                            "id": 2001,
                            "episode_number": 1,
                            "name": "Episode 1",
                            "overview": "First episode"
                        }
                    ]
                }"""
                )

                else -> respondError(HttpStatusCode.NotFound)
            }
        }

        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            expectSuccess = false
        }

        val dataSource = RemoteTvSeriesDataSourceImpl(client, baseUrl, languageProvider)

        // When
        val result = dataSource.getTvSeriesSeasonDetails(seriesId = 1, seasonNumber = 2)

        // Then
        assertEquals(2, result.seasonNumber)
        assertEquals("Season 2", result.name)
        assertEquals(1, result.episodes.size)
        assertEquals("Episode 1", result.episodes[0].name)
    }

    @Test
    fun `getTvSeries should return TvSeriesDto when response is valid`() = runTest {
        // Given
        val mockEngine = MockEngine { request ->
            val url = request.url.toString()
            if (url.contains("/tv/1")) {
                respondJson(
                    """{
                    "id": 1,
                    "name": "Breaking Bad",
                    "overview": "A high school chemistry teacher...",
                    "poster_path": "/poster.jpg"
                }"""
                )
            } else {
                respondError(HttpStatusCode.NotFound)
            }
        }

        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }

        val dataSource = RemoteTvSeriesDataSourceImpl(client, baseUrl, languageProvider)

        // When
        val result = dataSource.getTvSeries(1)

        // Then
        assertEquals(1, result.id)
        assertEquals("Breaking Bad", result.name)
        assertEquals("/poster.jpg", result.posterPath)
    }

    @Test
    fun `getTvSeriesVideos should return list of videos when response is valid`() = runTest {
        // Given
        val mockEngine = MockEngine { request ->
            val url = request.url.toString()
            if (url.contains("/tv/1/videos")) {
                respondJson(
                    """{
                    "id": 1,
                    "results": [
                        {
                            "id": "abc123",
                            "key": "video_key_1",
                            "name": "Trailer 1",
                            "site": "YouTube",
                            "type": "Trailer"
                        }
                    ]
                }"""
                )
            } else {
                respondError(HttpStatusCode.NotFound)
            }
        }

        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }

        val dataSource = RemoteTvSeriesDataSourceImpl(client, baseUrl, languageProvider)

        // When
        val videos = dataSource.getTvSeriesVideos(1)

        // Then
        assertEquals(1, videos.size)
        assertEquals("Trailer 1", videos[0].name)
        assertEquals("YouTube", videos[0].site)
    }

    @Test
    fun `getEpisodeImages_shouldReturnListOfImages_whenValidResponse`() = runTest {
        val seriesId = 10
        val seasonNumber = 1
        val episodeNumber = 3

        val mockEngine = MockEngine { request ->
            val url = request.url.toString().substringBefore("?")
            when {
                url.endsWith("/tv/$seriesId/season/$seasonNumber/episode/$episodeNumber/images") -> respondJson(
                    """{
                    "id": $seriesId,
                    "backdrops": [
                        {
                            "aspect_ratio": 1.78,
                            "file_path": "/episode_image1.jpg",
                            "height": 1080,
                            "width": 1920
                        },
                        {
                            "aspect_ratio": 1.78,
                            "file_path": "/episode_image2.jpg",
                            "height": 720,
                            "width": 1280
                        }
                    ]
                }"""
                )

                else -> respondError(HttpStatusCode.NotFound)
            }
        }

        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            expectSuccess = false
        }

        val dataSource = RemoteTvSeriesDataSourceImpl(client, baseUrl, languageProvider)

        val result = dataSource.getEpisodeImages(seriesId, seasonNumber, episodeNumber)

        assertEquals(2, result.size)
        assertEquals("/episode_image1.jpg", result[0].filePath)
        assertEquals("/episode_image2.jpg", result[1].filePath)
    }
    

    @Test
    fun `getTvSeriesReviews_shouldReturnEmptyList_whenNoReviews`() = runTest {
        // Given - Mock empty reviews response
        val emptyReviewsEngine = MockEngine { request ->
            val url = request.url.toString().substringBefore("?")
            when {
                url.endsWith("/tv/2/reviews") -> respondJson(
                    """{
                        "id": 2,
                        "page": 1,
                        "results": [],
                        "total_pages": 0,
                        "total_results": 0
                    }"""
                )

                else -> respondError(HttpStatusCode.NotFound)
            }
        }

        val emptyClient = HttpClient(emptyReviewsEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            expectSuccess = false
        }

        val emptyDataSource = RemoteTvSeriesDataSourceImpl(emptyClient, baseUrl, languageProvider)

        // When
        val result = emptyDataSource.getTvSeriesReviews(2)

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `getTvSeriesReviews_shouldHandleLargeReviewCount`() = runTest {
        // Given - Mock many reviews response
        val largeReviewsEngine = MockEngine { request ->
            val url = request.url.toString().substringBefore("?")
            when {
                url.endsWith("/tv/3/reviews") -> {
                    val reviews = (1..50).joinToString(",") { index ->
                        """{
                            "author": "Reviewer $index",
                            "author_details": {
                                "name": "Reviewer $index",
                                "username": "reviewer$index",
                                "avatar_path": "/path/to/avatar$index.jpg",
                                "rating": ${5.0 + index * 0.1}
                            },
                            "content": "Review content $index",
                            "created_at": "2023-01-${String.format("%02d", index)}T00:00:00.000Z",
                            "id": "review$index",
                            "updated_at": "2023-01-${String.format("%02d", index)}T00:00:00.000Z",
                            "url": "https://example.com/review$index"
                        }"""
                    }
                    respondJson(
                        """{
                            "id": 3,
                            "page": 1,
                            "results": [$reviews],
                            "total_pages": 10,
                            "total_results": 500
                        }"""
                    )
                }

                else -> respondError(HttpStatusCode.NotFound)
            }
        }

        val largeClient = HttpClient(largeReviewsEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            expectSuccess = false
        }

        val largeDataSource = RemoteTvSeriesDataSourceImpl(largeClient, baseUrl, languageProvider)

        // When
        val result = largeDataSource.getTvSeriesReviews(3)

        // Then
        assertEquals(50, result.size)
        assertEquals("Reviewer 1", result[0].author)
        assertEquals("Reviewer 50", result[49].author)
        assertEquals("Review content 1", result[0].content)
        assertEquals("Review content 50", result[49].content)
    }

    @Test
    fun `getTvSeriesReviews_shouldHandleNullValues`() = runTest {
        // Given - Mock response with null values
        val nullValuesEngine = MockEngine { request ->
            val url = request.url.toString().substringBefore("?")
            when {
                url.endsWith("/tv/4/reviews") -> respondJson(
                    """{
                        "id": 4,
                        "page": 1,
                        "results": [
                            {
                                "author": "Reviewer with nulls",
                                "author_details": {
                                    "name": "Reviewer with nulls",
                                    "username": null,
                                    "avatar_path": null,
                                    "rating": null
                                },
                                "content": "Review with null values",
                                "created_at": "2023-01-01T00:00:00.000Z",
                                "id": "review_with_nulls",
                                "updated_at": "2023-01-01T00:00:00.000Z",
                                "url": "https://example.com/review_with_nulls"
                            }
                        ],
                        "total_pages": 1,
                        "total_results": 1
                    }"""
                )

                else -> respondError(HttpStatusCode.NotFound)
            }
        }

        val nullClient = HttpClient(nullValuesEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            expectSuccess = false
        }

        val nullDataSource = RemoteTvSeriesDataSourceImpl(nullClient, baseUrl, languageProvider)

        // When
        val result = nullDataSource.getTvSeriesReviews(4)

        // Then
        assertEquals(1, result.size)
        assertEquals("Reviewer with nulls", result[0].author)
        assertEquals("Review with null values", result[0].content)
        assertEquals("", result[0].authorDetails.username ?: "")
        assertEquals(null, result[0].authorDetails.avatarPath)
        assertEquals(null, result[0].authorDetails.rating)
    }

    @Test
    fun `getTvSeriesReviews_shouldHandleDifferentRatingValues`() = runTest {
        // Given - Mock response with different rating values
        val ratingsEngine = MockEngine { request ->
            val url = request.url.toString().substringBefore("?")
            when {
                url.endsWith("/tv/5/reviews") -> respondJson(
                    """{
                        "id": 5,
                        "page": 1,
                        "results": [
                            {
                                "author": "High Rating Reviewer",
                                "author_details": {
                                    "name": "High Rating Reviewer",
                                    "username": "high_rating",
                                    "avatar_path": "/high.jpg",
                                    "rating": 10.0
                                },
                                "content": "Perfect series!",
                                "created_at": "2023-01-01T00:00:00.000Z",
                                "id": "high_rating_review",
                                "updated_at": "2023-01-01T00:00:00.000Z",
                                "url": "https://example.com/high_rating"
                            },
                            {
                                "author": "Low Rating Reviewer",
                                "author_details": {
                                    "name": "Low Rating Reviewer",
                                    "username": "low_rating",
                                    "avatar_path": "/low.jpg",
                                    "rating": 1.0
                                },
                                "content": "Not good",
                                "created_at": "2023-01-02T00:00:00.000Z",
                                "id": "low_rating_review",
                                "updated_at": "2023-01-02T00:00:00.000Z",
                                "url": "https://example.com/low_rating"
                            },
                            {
                                "author": "No Rating Reviewer",
                                "author_details": {
                                    "name": "No Rating Reviewer",
                                    "username": "no_rating",
                                    "avatar_path": "/no.jpg",
                                    "rating": null
                                },
                                "content": "No rating given",
                                "created_at": "2023-01-03T00:00:00.000Z",
                                "id": "no_rating_review",
                                "updated_at": "2023-01-03T00:00:00.000Z",
                                "url": "https://example.com/no_rating"
                            }
                        ],
                        "total_pages": 1,
                        "total_results": 3
                    }"""
                )

                else -> respondError(HttpStatusCode.NotFound)
            }
        }

        val ratingsClient = HttpClient(ratingsEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            expectSuccess = false
        }

        val ratingsDataSource =
            RemoteTvSeriesDataSourceImpl(ratingsClient, baseUrl, languageProvider)

        // When
        val result = ratingsDataSource.getTvSeriesReviews(5)

        // Then
        assertEquals(3, result.size)
        assertEquals(10.0f, result[0].authorDetails.rating)
        assertEquals(1.0f, result[1].authorDetails.rating)
        assertEquals(null, result[2].authorDetails.rating)
        assertEquals("High Rating Reviewer", result[0].author)
        assertEquals("Low Rating Reviewer", result[1].author)
        assertEquals("No Rating Reviewer", result[2].author)
    }

    @Test
    fun `reviewsResponse_shouldContainPaginationInfo`() = runTest {
        // When
        val result = dataSource.getTvSeriesReviews(1)

        assertEquals(2, result.size)
        assertTrue(result.isNotEmpty())
    }

    @Test
    fun `reviewsResponse_shouldHandleMultiplePagesConcept`() = runTest {
        // Given - Mock different pages (conceptually)
        val page1Engine = MockEngine { request ->
            val url = request.url.toString().substringBefore("?")
            when {
                url.endsWith("/tv/6/reviews") -> respondJson(
                    """{
                        "id": 6,
                        "page": 1,
                        "results": [
                            {
                                "author": "Page 1 Reviewer",
                                "author_details": {"name": "Page 1 Reviewer", "username": "page1", "rating": 8.0},
                                "content": "Page 1 content",
                                "created_at": "2023-01-01T00:00:00.000Z",
                                "id": "page1_review",
                                "updated_at": "2023-01-01T00:00:00.000Z",
                                "url": "https://example.com/page1"
                            }
                        ],
                        "total_pages": 3,
                        "total_results": 30
                    }"""
                )

                else -> respondError(HttpStatusCode.NotFound)
            }
        }

        val page1Client = HttpClient(page1Engine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            expectSuccess = false
        }

        val page1DataSource = RemoteTvSeriesDataSourceImpl(page1Client, baseUrl, languageProvider)

        // When
        val result = page1DataSource.getTvSeriesReviews(6)

        // Then
        assertEquals(1, result.size)
        assertEquals("Page 1 Reviewer", result[0].author)
        assertEquals("Page 1 content", result[0].content)
    }

    @Test
    fun `getTvSeriesByGenre should return tv series list for given genre`() = runTest {
        // Given
        val genreEngine = MockEngine { request ->
            val url = request.url.fullPath
            if (url.startsWith("/3/discover/tv") && request.url.parameters["with_genres"] == "18") {
                respondJson(
                    """
                {
                  "page": 1,
                  "results": [
                    {
                      "id": 101,
                      "name": "Genre Series 1",
                      "overview": "Test overview",
                      "poster_path": "/genre1.jpg",
                      "backdrop_path": "/bg.jpg",
                      "first_air_date": "2022-01-01",
                      "vote_average": 8.5,
                      "vote_count": 200,
                      "popularity": 150.0,
                      "genres": [],
                      "number_of_seasons": 2,
                      "number_of_episodes": 20
                    }
                  ],
                  "total_pages": 1,
                  "total_results": 1
                }
                """.trimIndent()
                )
            } else {
                respondError(HttpStatusCode.NotFound)
            }
        }

        val genreClient = HttpClient(genreEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            expectSuccess = false
        }

        val genreDataSource = RemoteTvSeriesDataSourceImpl(genreClient, baseUrl, languageProvider)

        // When
        val result = genreDataSource.getTvSeriesByGenre(18)

        // Then
        assertEquals(1, result.size)
        assertEquals(101, result[0].id)
        assertEquals("Genre Series 1", result[0].name)
    }


    // Must be an extension on MockRequestHandleScope and return HttpResponseData
    private fun MockRequestHandleScope.respondJson(content: String) = respond(
        content = ByteReadChannel(content),
        status = HttpStatusCode.OK,
        headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
    )
}
