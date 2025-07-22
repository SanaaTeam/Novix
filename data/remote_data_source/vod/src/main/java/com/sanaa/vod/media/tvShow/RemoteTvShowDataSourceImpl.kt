package com.sanaa.vod.media.tvShow

import com.sanaa.data.remotedatasource.vod.BuildConfig
import com.sanaa.preferences.service.LanguageProvider
import com.sanaa.vod.dataSource.remote.dto.ActorDto
import com.sanaa.vod.dataSource.remote.dto.EpisodeDto
import com.sanaa.vod.dataSource.remote.dto.ImageDto
import com.sanaa.vod.dataSource.remote.dto.ReviewDto
import com.sanaa.vod.dataSource.remote.dto.SeasonDto
import com.sanaa.vod.dataSource.remote.dto.TvShowDto
import com.sanaa.vod.dataSource.remote.dto.VideoDto
import com.sanaa.vod.dataSource.remote.tvShow.RemoteTvSeriesDataSource
import com.sanaa.vod.media.tvShow.response.GenreTvSeriesResponse
import com.sanaa.vod.media.tvShow.response.ImagesResponse
import com.sanaa.vod.media.tvShow.response.TvSeriesCastResponse
import com.sanaa.vod.media.tvShow.response.TvSeriesGuestOfStarsResponse
import com.sanaa.vod.media.tvShow.response.TvShowReviewsResponse
import com.sanaa.vod.media.tvShow.response.TvShowVideosResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.util.reflect.TypeInfo
import io.ktor.util.reflect.typeInfo

class RemoteTvShowDataSourceImpl(
    private val client: HttpClient,
    private val languageProvider: LanguageProvider
) : RemoteTvSeriesDataSource {

    override suspend fun getTvSeries(id: Int): TvShowDto =
        fetchTvSeries("tv", id)

    override suspend fun getTvSeriesVideos(id: Int): List<VideoDto> =
        fetchTvSeries<TvShowVideosResponse>("tv", id, "videos", false).results

    override suspend fun getTvSeriesSeasonDetails(
        seriesId: Int, seasonNumber: Int
    ): SeasonDto =
        fetchTvSeries("tv", seriesId, "season/$seasonNumber")

    override suspend fun getTvSeriesImages(id: Int): List<ImageDto> =
        fetchTvSeries<ImagesResponse>("tv", id, "images", false).backdrops

    override suspend fun getTvSeriesByGenre(genreId: Int): List<TvShowDto> =
        client.get("${BuildConfig.TMDB_URL}/discover/tv") {
            parameter("with_genres", genreId)
            parameter("language", languageProvider.getCurrentLanguage())
            parameter("api_key", BuildConfig.TMDB_API_KEY)
        }.body<GenreTvSeriesResponse>().results

    override suspend fun getTvSeriesReviews(id: Int): List<ReviewDto> =
        fetchTvSeries<TvShowReviewsResponse>("tv", id, "reviews").results

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
    ): List<ImageDto> =
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