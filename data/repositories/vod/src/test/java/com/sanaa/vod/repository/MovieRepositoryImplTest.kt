package com.sanaa.vod.repository

import com.google.common.truth.Truth.assertThat
import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import com.sanaa.vod.dataSource.local.cache.LocalCachedContentDataSource
import com.sanaa.vod.dataSource.local.cache.dto.CachedContentMetadataLocalDto.Category
import com.sanaa.vod.dataSource.local.cache.dto.MovieLocalDto
import com.sanaa.vod.dataSource.remote.RemoteMovieDataSource
import com.sanaa.vod.dataSource.remote.dto.GenreDto
import com.sanaa.vod.dataSource.remote.dto.ImageDto
import com.sanaa.vod.dataSource.remote.dto.VideoDto
import com.sanaa.vod.dataSource.remote.dto.actor.ActorDto
import com.sanaa.vod.dataSource.remote.dto.movie.MovieDto
import com.sanaa.vod.dataSource.remote.dto.review.AuthorDetailsDto
import com.sanaa.vod.dataSource.remote.dto.review.ReviewDto
import com.sanaa.vod.repository.mapper.cachedContent.toDomain
import com.sanaa.vod.repository.mapper.media.toDomain
import com.sanaa.vod.util.exceptions.ConnectionException
import exceptions.NoNetworkException
import exceptions.RetrievingDataFailureException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class MovieRepositoryImplTest {

    private lateinit var repository: MovieRepositoryImpl
    private val preferences: PreferencesManager = mockk()
    private val remote: RemoteMovieDataSource = mockk(relaxed = true)
    private val local: LocalCachedContentDataSource = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        repository = MovieRepositoryImpl(remote, local, preferences)
    }

    @Test
    fun `getMovieDetails returns expected movie`() = runTest {
        coEvery { remote.fetchMovieDetails(1) } returns sampleMovieDto

        val result = repository.getMovieDetails(1)

        assertThat(result.title).isEqualTo("Fight club")
    }

    @Test
    fun `getImages returns top images poster urls`() = runTest {
        coEvery { remote.fetchImagesUrl(1) } returns listOf(sampleImagesDto)

        val result = repository.getImageUrls(1, 2)

        assertThat(result.size).isEqualTo(1)
    }

    @Test
    fun `getMovieCast returns cast list`() = runTest {
        coEvery { remote.fetchCast(1) } returns sampleCastDto

        val result = repository.getMovieCast(1)

        assertThat(result.size).isEqualTo(2)
    }

    @Test
    fun `getSimilarMoviesByMovieId returns similar movies`() = runTest {
        coEvery { remote.fetchSimilarMoviesByMovieId(1, 1) } returns sampleSimilarDto

        val result = repository.getSimilarMoviesByMovieId(1, 1)

        assertThat(result.size).isEqualTo(2)
    }

    @Test
    fun `getReviewsByMovieId returns reviews`() = runTest {
        coEvery { remote.fetchReviewsByMovieId(1, 1) } returns listOf(sampleReviewDto)

        val result = repository.getReviewsByMovieId(1, 1)

        assertThat(result.first().authorName).isEqualTo("Critic A")
    }

    @Test
    fun `getMovieDetails throws NoNetworkException on ConnectionException`() = runTest {
        coEvery { remote.fetchMovieDetails(any()) } throws ConnectionException()

        assertThrows<NoNetworkException> { repository.getMovieDetails(1) }
    }

    @Test
    fun `getImages throws RetrievingDataFailureException on unknown error`() = runTest {
        coEvery { remote.fetchImagesUrl(any()) } throws RuntimeException("some error")

        assertThrows<RetrievingDataFailureException> { repository.getImageUrls(1, 2) }
    }

    @Test
    fun `getMoviesByCategory should return list of MovieDto `() = runTest {
        coEvery { remote.fetchMoviesByCategory(any(), any()) } returns sampleSimilarDto

        val result = repository.getMoviesByCategory(1, 1)

        assertThat(result).isNotEmpty()
    }


    @Test
    fun `getMovieTrailer returns null when no YouTube trailer found`() = runTest {
        coEvery { remote.fetchMovieTrailerUrl(1) } returns trailers

        val result = repository.getMovieTrailer(1)

        assertThat(result).isNull()
    }

    @Test
    fun `getMovieTrailer returns null when empty list returned`() = runTest {
        coEvery { remote.fetchMovieTrailerUrl(1) } returns emptyList()

        val result = repository.getMovieTrailer(1)

        assertThat(result).isNull()
    }

    @Test
    fun `getPopularMovies should return cached data if available when page is 1`() = runTest {
        val cached = listOf(sampleMovieLocalDto)
        coEvery { local.getCachedMovies(Category.POPULAR) } returns cached

        val result = repository.getPopularMovies(1)

        assertThat(result).isEqualTo(cached.map { it.toDomain() })
    }

    @Test
    fun `getPopularMovies should fetches remote when page is 1 and cache is empty`() = runTest {
        val movies = listOf(sampleMovieDto)
        coEvery { local.getCachedMovies(Category.POPULAR) } returns emptyList()
        coEvery { remote.fetchPopularMovies(1) } returns movies

        val result = repository.getPopularMovies(1)

        assertThat(result).isEqualTo(movies.map { it.toDomain() })
    }

    @Test
    fun `getPopularMovies should cache the new data when page is 1 and cache is empty`() = runTest {
        val movies = listOf(sampleMovieDto)
        coEvery { local.getCachedMovies(Category.POPULAR) } returns emptyList()
        coEvery { remote.fetchPopularMovies(1) } returns movies

        repository.getPopularMovies(1)

        coVerify(exactly = movies.size) { local.cacheMovie(any(), any()) }
    }

    @Test
    fun `getPopularMovies should fetch from remote if page is not 1`() = runTest {
        val movies = listOf(sampleMovieDto)
        coEvery { remote.fetchPopularMovies(2) } returns movies

        val result = repository.getPopularMovies(2)

        assertThat(result).isEqualTo(movies.map { it.toDomain() })
    }

    @Test
    fun `getPopularMovies should not cache the new data when page is not 1`() = runTest {
        val movies = listOf(sampleMovieDto)
        coEvery { remote.fetchPopularMovies(2) } returns movies

        repository.getPopularMovies(2)

        coVerify(exactly = 0) { local.cacheMovie(any(), any()) }
    }

    @Test
    fun `getTopRatedMovies should return cached data if available when page is 1 and genreId is null`() = runTest {
        val cached = listOf(sampleMovieLocalDto)
        coEvery { local.getCachedMovies(Category.TOP_RATED) } returns cached

        val result = repository.getTopRatedMovies(1, null)

        assertThat(result).isEqualTo(cached.map { it.toDomain() })
    }

    @Test
    fun `getTopRatedMovies should fetches remote when page is 1 and genreId is null and cache is empty`() = runTest {
        val movies = listOf(sampleMovieDto)
        coEvery { local.getCachedMovies(Category.TOP_RATED) } returns emptyList()
        coEvery { remote.fetchTopRatedMovies(1, null) } returns movies

        val result = repository.getTopRatedMovies(1, null)

        assertThat(result).isEqualTo(movies.map { it.toDomain() })
    }

    @Test
    fun `getTopRatedMovies should cache the new data when page is 1 and genreId is null and cache is empty`() = runTest {
        val movies = listOf(sampleMovieDto)
        coEvery { local.getCachedMovies(Category.TOP_RATED) } returns emptyList()
        coEvery { remote.fetchTopRatedMovies(1, null) } returns movies

        repository.getTopRatedMovies(1, null)

        coVerify(exactly = movies.size) { local.cacheMovie(any(), any()) }
    }

    @Test
    fun `getTopRatedMovies should not cache the new data when page is not 1`() = runTest {
        val movies = listOf(sampleMovieDto)
        coEvery { local.getCachedMovies(Category.TOP_RATED) } returns emptyList()
        coEvery { remote.fetchTopRatedMovies(2, null) } returns movies

        repository.getTopRatedMovies(2, null)

        coVerify(exactly = 0) { local.cacheMovie(any(), any()) }
    }

    @Test
    fun `getTopRatedMovies should not cache the new data when genreId is not null`() = runTest {
        val movies = listOf(sampleMovieDto)
        coEvery { local.getCachedMovies(Category.TOP_RATED) } returns emptyList()
        coEvery { remote.fetchTopRatedMovies(1, 1) } returns movies

        repository.getTopRatedMovies(1, 1)

        coVerify(exactly = 0) { local.cacheMovie(any(), any()) }
    }

    @Test
    fun `getTopRatedMovies should fetch from remote when page is not 1`() = runTest {
        val movies = listOf(sampleMovieDto)
        coEvery { remote.fetchTopRatedMovies(2, null) } returns movies

        val result = repository.getTopRatedMovies(2, null)

        assertThat(result).isEqualTo(movies.map { it.toDomain() })
    }

    @Test
    fun `getTopRatedMovies should fetch from remote when genreId is not null`() = runTest {
        val movies = listOf(sampleMovieDto)
        coEvery { remote.fetchTopRatedMovies(1, 1) } returns movies

        val result = repository.getTopRatedMovies(1, 1)

        assertThat(result).isEqualTo(movies.map { it.toDomain() })
    }

    @Test
    fun `getUpcomingMovies should return cached data if available when page is 1 and genreId is null`() = runTest {
        val cached = listOf(sampleMovieLocalDto)
        coEvery { local.getCachedMovies(Category.UPCOMING) } returns cached

        val result = repository.getUpcomingMovies(1, null)

        assertThat(result).isEqualTo(cached.map { it.toDomain() })
    }

    @Test
    fun `getUpcomingMovies should fetches remote when page is 1 and genreId is null and cache is empty`() = runTest {
        val movies = listOf(sampleMovieDto)
        coEvery { local.getCachedMovies(Category.UPCOMING) } returns emptyList()
        coEvery { remote.fetchUpcomingMovies(1, null) } returns movies

        val result = repository.getUpcomingMovies(1, null)

        assertThat(result).isEqualTo(movies.map { it.toDomain() })
    }

    @Test
    fun `getUpcomingMovies should cache the new data when page is 1 and genreId is null and cache is empty`() = runTest {
        val movies = listOf(sampleMovieDto)
        coEvery { local.getCachedMovies(Category.UPCOMING) } returns emptyList()
        coEvery { remote.fetchUpcomingMovies(1, null) } returns movies

        repository.getUpcomingMovies(1, null)

        coVerify(exactly = movies.size) { local.cacheMovie(any(), any()) }
    }

    @Test
    fun `getUpcomingMovies should not cache the new data when page is not 1`() = runTest {
        val movies = listOf(sampleMovieDto)
        coEvery { local.getCachedMovies(Category.UPCOMING) } returns emptyList()
        coEvery { remote.fetchUpcomingMovies(2, null) } returns movies

        repository.getUpcomingMovies(2, null)

        coVerify(exactly = 0) { local.cacheMovie(any(), any()) }
    }

    @Test
    fun `getUpcomingMovies should not cache the new data when genreId is not null`() = runTest {
        val movies = listOf(sampleMovieDto)
        coEvery { local.getCachedMovies(Category.UPCOMING) } returns emptyList()
        coEvery { remote.fetchUpcomingMovies(1, 1) } returns movies

        repository.getUpcomingMovies(1, 1)

        coVerify(exactly = 0) { local.cacheMovie(any(), any()) }
    }

    @Test
    fun `getUpcomingMovies should fetch from remote when page is not 1`() = runTest {
        val movies = listOf(sampleMovieDto)
        coEvery { remote.fetchUpcomingMovies(2, null) } returns movies

        val result = repository.getUpcomingMovies(2, null)

        assertThat(result).isEqualTo(movies.map { it.toDomain() })
    }

    @Test
    fun `getUpcomingMovies should fetch from remote when genreId is not null`() = runTest {
        val movies = listOf(sampleMovieDto)
        coEvery { remote.fetchUpcomingMovies(1, 1) } returns movies

        val result = repository.getUpcomingMovies(1, 1)

        assertThat(result).isEqualTo(movies.map { it.toDomain() })
    }


    @Test
    fun `getTrendingMovies returns empty list`() = runTest {
        val result = repository.getTrendingMovies(page = 1, genreId = 1)
        assertThat(result).isEmpty()
    }

    @Test
    fun `getMovieGenres returns list of genres`() = runTest {
        coEvery { remote.fetchMovieGenres() } returns dummyGenresDto

        val result = repository.getMovieGenres()

        assertThat(result).isNotEmpty()
    }

    companion object {
        private val sampleMovieDto = MovieDto(id = 1, title = "Fight club")
        private val sampleMovieLocalDto = MovieLocalDto(
            id = 1, title = "Fight club",
            posterImageUrl = "",
            releaseDate = null,
            imdbRating = 5f
        )
        private val sampleImagesDto = ImageDto(filePath = "/poster1.jpg")

        private val sampleCastDto = listOf<ActorDto>(
            ActorDto(name = "Actor A", id = 1), ActorDto(
                name = "Actor B", id = 2,
            )
        )

        private val sampleSimilarDto = listOf<MovieDto>(
            MovieDto(
                id = 1, title = "A"
            ), MovieDto(
                id = 2, title = "B"
            )
        )

        private val sampleReviewDto = ReviewDto(
            id = "1", content = "Nice movie!", authorDetails = AuthorDetailsDto(
                name = "Critic A", username = "critic_a", avatarPath = "/avatar.jpg", rating = 8.5f
            ), createdAt = "2024-01-01T00:00:00Z"
        )
        val trailers = listOf(
            VideoDto(type = "Teaser", site = "YouTube", key = "def456"),
            VideoDto(type = "Trailer", site = "Vimeo", key = "ghi789")
        )
        val dummyGenresDto = listOf(
            GenreDto(id = 1, name = "Action"),
            GenreDto(id = 2, name = "Drama")
        )
    }
}