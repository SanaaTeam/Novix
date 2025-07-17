package com.sanaa.series

import com.example.env_config.service.LanguageProvider
import com.sanaa.series.data_source.remote.RemoteTvSeriesDataSource
import com.sanaa.series.dto.*
import com.sanaa.series.response.*
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.util.reflect.TypeInfo
import io.ktor.util.reflect.typeInfo

class RemoteTvSeriesDataSourceImpl(
    private val client: HttpClient,
    private val baseUrl: String,
    private val languageProvider: LanguageProvider
) : RemoteTvSeriesDataSource {

    override suspend fun getTvSeries(id: Int): TvSeriesDto =
        fetchTvSeries("tv", id)

    override suspend fun getTvSeriesVideos(id: Int): List<TvSeriesVideoDto> =
        fetchTvSeries<TvSeriesVideosResponse>("tv", id, "videos").results

    override suspend fun getTvSeriesSeasonDetails(
        seriesId: Int, seasonNumber: Int
    ): SeasonDto =
        fetchTvSeries("tv", seriesId, "season/$seasonNumber")

    override suspend fun getTvSeriesImages(id: Int): List<TvSeriesImageDto> =
        fetchTvSeries<ImagesResponse>("tv", id, "images").backdrops

    override suspend fun getTvSeriesByGenre(genreId: Int): List<TvSeriesDto> =
        client.get("$baseUrl/discover/tv") {
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
            "season/$seasonNumber/episode/$episodeNumber/images"
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
        subPath: String? = null
    ): T = fetchInternal(path, id, subPath, typeInfo<T>())

    private suspend fun <T> fetchInternal(
        path: String,
        id: Int,
        subPath: String?,
        type: TypeInfo
    ): T {
        val fullPath = buildString {
            append("$baseUrl/$path/$id")
            if (!subPath.isNullOrBlank()) append("/$subPath")
        }

        return client.get(fullPath) {
            parameter("language", languageProvider.getCurrentLanguage())
            parameter("api_key", BuildConfig.TMDB_API_KEY)
        }.body(type)
    }
}
