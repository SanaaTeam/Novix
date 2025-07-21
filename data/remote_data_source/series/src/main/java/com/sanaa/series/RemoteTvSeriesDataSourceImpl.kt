package com.sanaa.series

import com.sanaa.preferences.service.LanguageProvider
import com.sanaa.series.dataSource.remote.RemoteTvSeriesDataSource
import com.sanaa.series.dto.ActorDto
import com.sanaa.series.dto.EpisodeDto
import com.sanaa.series.dto.ReviewDto
import com.sanaa.series.dto.SeasonDto
import com.sanaa.series.dto.TvSeriesDto
import com.sanaa.series.dto.TvSeriesImageDto
import com.sanaa.series.dto.TvSeriesVideoDto
import com.sanaa.series.response.GenreTvSeriesResponse
import com.sanaa.series.response.ImagesResponse
import com.sanaa.series.response.TvSeriesCastResponse
import com.sanaa.series.response.TvSeriesGuestOfStarsResponse
import com.sanaa.series.response.TvSeriesReviewsResponse
import com.sanaa.series.response.TvSeriesVideosResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.util.reflect.TypeInfo
import io.ktor.util.reflect.typeInfo

class RemoteTvSeriesDataSourceImpl(
    private val client: HttpClient,
    private val languageProvider: LanguageProvider
) : RemoteTvSeriesDataSource {

    override suspend fun getTvSeries(id: Int): TvSeriesDto =
        fetchTvSeries("tv", id)

    override suspend fun getTvSeriesVideos(id: Int): List<TvSeriesVideoDto> =
        fetchTvSeries<TvSeriesVideosResponse>("tv", id, "videos",false).results

    override suspend fun getTvSeriesSeasonDetails(
        seriesId: Int, seasonNumber: Int
    ): SeasonDto =
        fetchTvSeries("tv", seriesId, "season/$seasonNumber")

    override suspend fun getTvSeriesImages(id: Int): List<TvSeriesImageDto> =
        fetchTvSeries<ImagesResponse>("tv", id, "images",false).backdrops

    override suspend fun getTvSeriesByGenre(genreId: Int): List<TvSeriesDto> =
        client.get("${BuildConfig.TMDB_URL}/discover/tv") {
            parameter("with_genres", genreId)
            parameter("language", languageProvider.getCurrentLanguage())
            parameter("api_key", BuildConfig.TMDB_API_KEY)
        }.body<GenreTvSeriesResponse>().results

    override suspend fun getTvSeriesReviews(id: Int): List<ReviewDto> =
        fetchTvSeries<TvSeriesReviewsResponse>("tv", id, "reviews").results

    override suspend fun getTvSeriesCast(id: Int): List<ActorDto> =
        fetchTvSeries<TvSeriesCastResponse>("tv", id, "credits").cast

    override suspend fun getEpisodeDetails(
        seriesId: Int, seasonNumber: Int, episodeNumber: Int
    ): EpisodeDto =
        fetchTvSeries(
            "tv",
            seriesId,
            "season/$seasonNumber/episode/$episodeNumber"
        )

    override suspend fun getEpisodeImages(
        seriesId: Int, seasonNumber: Int, episodeNumber: Int
    ): List<TvSeriesImageDto> =
        fetchTvSeries<ImagesResponse>(
            "tv",
            seriesId,
            "season/$seasonNumber/episode/$episodeNumber/images",
            false
        ).backdrops

    override suspend fun getEpisodeGuestsOfHonor(
        seriesId: Int, seasonNumber: Int, episodeNumber: Int
    ): List<ActorDto> =
        fetchTvSeries<TvSeriesGuestOfStarsResponse>(
            "tv",
            seriesId,
            "season/$seasonNumber/episode/$episodeNumber/credits"
        ).guestStars


    private suspend inline fun <reified T> fetchTvSeries(
        path: String,
        id: Int,
        subPath: String? = null,
        withLanguage: Boolean = true
    ): T = fetchInternal(path, id, subPath, typeInfo<T>(), withLanguage)

    private suspend fun <T> fetchInternal(
        path: String,
        id: Int,
        subPath: String?,
        type: TypeInfo,
        withLanguage: Boolean
    ): T {
        val fullPath = buildString {
            append("${BuildConfig.TMDB_URL}/$path/$id")
            if (!subPath.isNullOrBlank()) append("/$subPath")
        }

        return client.get(fullPath) {
            if (withLanguage) {
                parameter("language", languageProvider.getCurrentLanguage())
            }
            parameter("api_key", BuildConfig.TMDB_API_KEY)
        }.body(type)
    }
}
