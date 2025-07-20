package com.sanaa.search.mapper

import entity.Genre

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