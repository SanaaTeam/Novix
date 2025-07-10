package com.sanaa.search.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MoviesLocalDto(
    @PrimaryKey
    @ColumnInfo(name = "movie_id")
    val movieId: Int,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "image_path")
    val imagePath: String?,

    @ColumnInfo(name = "release_year")
    val releaseYear: Int?,

    @ColumnInfo(name = "genres")
    val genres: String?,

    @ColumnInfo(name = "imdb_rating")
    val imdbRating: Float?
) 