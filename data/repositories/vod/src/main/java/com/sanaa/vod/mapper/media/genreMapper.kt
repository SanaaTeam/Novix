package com.sanaa.vod.mapper.media

import entity.Genre

object GenreId {
    const val ACTION = 28
    const val ADVENTURE = 12
    const val COMEDY = 35
    const val DRAMA = 18
    const val HORROR = 27
    const val SCIENCE_FICTION = 878
    const val FANTASY = 14
    const val ROMANCE = 10749
    const val THRILLER = 53
    const val DOCUMENTARY = 99
    const val ANIMATION = 16
    const val CRIME = 80
    const val FAMILY = 10751
    const val HISTORY = 36
    const val KIDS = 10762
    const val MYSTERY = 9648
    const val MUSIC = 10402
    const val NEWS = 10763
    const val REALITY = 10764
    const val SOAP = 10766
    const val TALK = 10767
    const val WAR = 10752
    const val WAR_AND_POLITICS = 10768
    const val WESTERN = 37
    const val TV_MOVIE = 10770
    const val ACTION_AND_ADVENTURE = 10759
    const val SCI_FI_AND_FANTASY = 10765
}

fun Genre.toDtoId(): Int {
    return when (this) {
        Genre.ACTION -> 28
        Genre.ADVENTURE -> 12
        Genre.COMEDY -> 35
        Genre.DRAMA -> 18
        Genre.HORROR -> 27
        Genre.SCIENCE_FICTION -> 878
        Genre.FANTASY -> 14
        Genre.ROMANCE -> 10749
        Genre.THRILLER -> 53
        Genre.DOCUMENTARY -> 99
        Genre.ANIMATION -> 16
        Genre.CRIME -> 80
        Genre.FAMILY -> 10751
        Genre.HISTORY -> 36
        Genre.KIDS -> 10762
        Genre.MYSTERY -> 9648
        Genre.MUSIC -> 10402
        Genre.NEWS -> 10763
        Genre.REALITY -> 10764
        Genre.SOAP -> 10766
        Genre.TALK -> 10767
        Genre.WAR -> 10752
        Genre.WAR_AND_POLITICS -> 10768
        Genre.WESTERN -> 37
        Genre.TV_MOVIE -> 10770
        Genre.ACTION_AND_ADVENTURE -> 10759
        Genre.SCI_FI_AND_FANTASY -> 10765
    }
}


fun Int.toGenre(): Genre? {
    return when (this) {
        GenreId.ACTION -> Genre.ACTION
        GenreId.ADVENTURE -> Genre.ADVENTURE
        GenreId.COMEDY -> Genre.COMEDY
        GenreId.DRAMA -> Genre.DRAMA
        GenreId.HORROR -> Genre.HORROR
        GenreId.SCIENCE_FICTION -> Genre.SCIENCE_FICTION
        GenreId.FANTASY -> Genre.FANTASY
        GenreId.ROMANCE -> Genre.ROMANCE
        GenreId.THRILLER -> Genre.THRILLER
        GenreId.DOCUMENTARY -> Genre.DOCUMENTARY
        GenreId.ANIMATION -> Genre.ANIMATION
        GenreId.CRIME -> Genre.CRIME
        GenreId.FAMILY -> Genre.FAMILY
        GenreId.HISTORY -> Genre.HISTORY
        GenreId.KIDS -> Genre.KIDS
        GenreId.MYSTERY -> Genre.MYSTERY
        GenreId.MUSIC -> Genre.MUSIC
        GenreId.NEWS -> Genre.NEWS
        GenreId.REALITY -> Genre.REALITY
        GenreId.SOAP -> Genre.SOAP
        GenreId.TALK -> Genre.TALK
        GenreId.WAR -> Genre.WAR
        GenreId.WAR_AND_POLITICS -> Genre.WAR_AND_POLITICS
        GenreId.WESTERN -> Genre.WESTERN
        GenreId.TV_MOVIE -> Genre.TV_MOVIE
        GenreId.ACTION_AND_ADVENTURE -> Genre.ACTION_AND_ADVENTURE
        GenreId.SCI_FI_AND_FANTASY -> Genre.SCI_FI_AND_FANTASY
        else -> null
    }
}

