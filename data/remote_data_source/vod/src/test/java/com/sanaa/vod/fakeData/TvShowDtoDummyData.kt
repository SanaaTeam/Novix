package com.sanaa.vod.fakeData

import com.sanaa.vod.dataSource.remote.dto.ActorDto
import com.sanaa.vod.dataSource.remote.dto.EpisodeDto
import com.sanaa.vod.dataSource.remote.dto.ImageDto
import com.sanaa.vod.dataSource.remote.dto.ReviewDto
import com.sanaa.vod.dataSource.remote.dto.SeasonDto
import com.sanaa.vod.dataSource.remote.dto.TvShowDto
import com.sanaa.vod.dataSource.remote.dto.VideoDto
import com.sanaa.vod.media.tvShow.response.GenreTvShowResponse
import com.sanaa.vod.media.tvShow.response.TvShowCastResponse
import com.sanaa.vod.media.tvShow.response.TvShowGuestOfStarsResponse
import com.sanaa.vod.media.tvShow.response.TvShowImagesResponse
import com.sanaa.vod.media.tvShow.response.TvShowReviewsResponse
import com.sanaa.vod.media.tvShow.response.TvShowVideosResponse

 object TvShowDtoDummyData{
    val dummyTvShowDto = TvShowDto(
        id = 1,
        name = "name",
        overview = "overview",
        posterPath = "posterPath",
        voteAverage = 1f,
    )
    val dummyTvShowImagesResponse = TvShowImagesResponse(
        backdrops = listOf(
            ImageDto(
                filePath = "filePath",
            )
        )
    )
    val dummyGenreTvShowResponse = GenreTvShowResponse(
        results = listOf(
            TvShowDto(
                id = 1,
                name = "name",
                overview = "overview",
            )
        ), page = 1
    )
    val dummyTvShowReviewsResponse = TvShowReviewsResponse(
        results = listOf(
            ReviewDto(
                id = "1",
                author = "author",
                content = "content",
                createdAt = "createdAt",
            )

        ), id = 1, page = 1
    )
    val dummyTvShowCastResponse = TvShowCastResponse(
        cast = listOf(
            ActorDto(
                id = 1,
                name = "name",
            )
        )
    )
    val dummyEpisodeDto = EpisodeDto(
        id = 1,
        name = "name",
    )
    val dummyGuestOfHonor = TvShowGuestOfStarsResponse(
        guestStars = listOf(
            ActorDto(
                id = 1,
                name = "name",
            )
        )
    )
    val dummyTvShowVideosResponse = TvShowVideosResponse(
        id = 1, results = listOf(
            VideoDto(
                id = "1",
                key = "key",
                name = "name",
            )
        )
    )

    val dummySeasonDto = SeasonDto(
        id = 1,
        name = "name",
    )
}