package entity

sealed interface MediaItem {
    val id: Int
    val title: String
    val posterImageUrl: String?

    data class MovieItem(val movie: Movie) : MediaItem {
        override val id: Int get() = movie.id
        override val title: String get() = movie.title
        override val posterImageUrl: String? get() = movie.posterImageUrl
    }

    data class TvSeriesItem(val series: TvSeries) : MediaItem {
        override val id: Int get() = series.id
        override val title: String get() = series.title
        override val posterImageUrl: String? get() = series.posterImageUrl
    }
}