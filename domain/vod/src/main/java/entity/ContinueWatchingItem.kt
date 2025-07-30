package entity

data class ContinueWatchingItem(
    val media: ContinuableMedia,
    val episodeId: Int? = null
)

sealed class ContinuableMedia {
    data class MovieItem(val movie: Movie) : ContinuableMedia()
    data class TvSeriesItem(val series: TvSeries) : ContinuableMedia()
}

enum class MediaType {
    MOVIE, TV_SERIES
}