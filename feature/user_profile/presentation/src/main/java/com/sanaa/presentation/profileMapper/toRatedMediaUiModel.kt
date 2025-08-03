package com.sanaa.presentation.profileMapper

import com.sanaa.presentation.model.RatedMediaUiModel
import entity.Movie
import entity.TvSeries

fun Movie.toRatedMediaUiModel(): RatedMediaUiModel {
    return RatedMediaUiModel(
        id = this.id,
        posterImageUrl = this.posterImageUrl,
        title = this.title,
        rating = this.rating,
        mediaType = "movie"
    )
}
fun TvSeries.toRatedMediaUiModel(): RatedMediaUiModel {
    return RatedMediaUiModel(
        id = this.id,
        posterImageUrl = this.posterImageUrl,
        title = this.title,
        rating = this.rating,
        mediaType = "tv_show"
    )
}