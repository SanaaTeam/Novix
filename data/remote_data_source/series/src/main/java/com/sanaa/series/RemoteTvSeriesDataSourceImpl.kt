package com.sanaa.series

import com.example.env_config.service.LanguageProvider
import com.sanaa.series.data_source.remote.RemoteTvSeriesDataSource
import com.sanaa.series.dto.ActorDto
import com.sanaa.series.dto.EpisodeDto
import com.sanaa.series.dto.ImageDto
import com.sanaa.series.dto.ReviewDto
import com.sanaa.series.dto.SeasonDto
import com.sanaa.series.dto.TvSeriesDto
import com.sanaa.series.dto.VideoDto
import com.sanaa.series.response.GenreTvSeriesResponse
import com.sanaa.series.response.ImagesResponse
import com.sanaa.series.response.TvSeriesCastResponse
import com.sanaa.series.response.TvSeriesGuestOfStarsResponse
import com.sanaa.series.response.TvSeriesReviewsResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class RemoteTvSeriesDataSourceImpl(
    private val client: HttpClient,
    private val baseUrl: String,
    private val languageProvider: LanguageProvider
) : RemoteTvSeriesDataSource {

    private suspend inline fun <reified T> fetchTvSeries(
        path: String, id: Int, subPath: String? = null
    ): T {
        val fullPath = buildString {
            append("$baseUrl/$path/$id")
            if (!subPath.isNullOrBlank()) append("/$subPath")
        }

        return client.get(fullPath) {
            parameter("language", languageProvider.getCurrentLanguage())
            parameter("api_key", BuildConfig.TMDB_API_KEY)
        }.body<T>()
    }

    override suspend fun getTvSeries(id: Int): TvSeriesDto {
        return fetchTvSeries(path = "tv", id = id)
    }

    override suspend fun getTvSeriesVideos(id: Int): List<VideoDto> {
        return fetchTvSeries(path = "tv", id = id, subPath = "videos")
    }

    override suspend fun getTvSeriesSeasonDetails(
        seriesId: Int, seasonNumber: Int
    ): SeasonDto {
        return fetchTvSeries(path = "tv", id = seriesId, subPath = "season/$seasonNumber")
    }

    override suspend fun getTvSeriesImages(id: Int): List<ImageDto> {
        return fetchTvSeries<ImagesResponse>(
            path = "tv", id = id, subPath = "images"
        ).backdrops
    }

    override suspend fun getTvSeriesByGenre(genreId: Int): List<TvSeriesDto> {
        return client.get("$baseUrl/discover/tv") {
            parameter("with_genres", genreId)
            parameter("language", languageProvider.getCurrentLanguage())
            parameter("api_key", BuildConfig.TMDB_API_KEY)
        }.body<GenreTvSeriesResponse>().results
    }

    override suspend fun getTvSeriesReviews(id: Int): List<ReviewDto> {
        return fetchTvSeries<TvSeriesReviewsResponse>(
            path = "tv", id = id, subPath = "reviews"
        ).results
    }

    override suspend fun getTvSeriesCast(id: Int): List<ActorDto> {
        return fetchTvSeries<TvSeriesCastResponse>(
            path = "tv", id = id, subPath = "credits"
        ).cast
    }

    override suspend fun getEpisodeDetails(
        seriesId: Int, seasonNumber: Int, episodeNumber: Int
    ): EpisodeDto {
        return fetchTvSeries(
            path = "tv", id = seriesId, subPath = "season/$seasonNumber/episode/$episodeNumber"
        )
    }

    override suspend fun getEpisodeImages(
        seriesId: Int, seasonNumber: Int, episodeNumber: Int
    ): List<ImageDto> {
        return fetchTvSeries<ImagesResponse>(
            path = "tv",
            id = seriesId,
            subPath = "season/$seasonNumber/episode/$episodeNumber/images"
        ).backdrops
    }

    override suspend fun getEpisodeGuestsOfHonor(
        seriesId: Int, seasonNumber: Int, episodeNumber: Int
    ): List<ActorDto> {
        return fetchTvSeries<TvSeriesGuestOfStarsResponse>(
            path = "tv",
            id = seriesId,
            subPath = "season/$seasonNumber/episode/$episodeNumber/credits"
        ).guestStars
    }
}