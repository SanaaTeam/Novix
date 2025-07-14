package com.sanaa.movies.mapper

import entity.Genre

fun Genre.toDtoId(): Int {
    return when(this) {
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
        Genre.WAR ->  10752
        Genre.WAR_AND_POLITICS -> 10768
        Genre.WESTERN -> 37
        Genre.TV_MOVIE -> 10770
        Genre.ACTION_AND_ADVENTURE -> 10759
        Genre.SCI_FI_AND_FANTASY -> 10765
    }
}


fun Int.toGenre(): Genre? {
    return when (this) {
        28 -> Genre.ACTION
        12 -> Genre.ADVENTURE
        35 -> Genre.COMEDY
        18 -> Genre.DRAMA
        27 -> Genre.HORROR
        878 -> Genre.SCIENCE_FICTION
        14 -> Genre.FANTASY
        10749 -> Genre.ROMANCE
        53 -> Genre.THRILLER
        99 -> Genre.DOCUMENTARY
        16 -> Genre.ANIMATION
        80 -> Genre.CRIME
        10751 -> Genre.FAMILY
        36 -> Genre.HISTORY
        10762 -> Genre.KIDS
        9648 -> Genre.MYSTERY
        10402 -> Genre.MUSIC
        10763 -> Genre.NEWS
        10764 -> Genre.REALITY
        10766 -> Genre.SOAP
        10767 -> Genre.TALK
        10752 -> Genre.WAR
        10768 -> Genre.WAR_AND_POLITICS
        37 -> Genre.WESTERN
        10770 -> Genre.TV_MOVIE
        10759 -> Genre.ACTION_AND_ADVENTURE
        10765 -> Genre.SCI_FI_AND_FANTASY
        else -> null
    }
}
