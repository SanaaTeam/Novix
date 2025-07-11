package com.sanaa.search.dataSource.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie")
data class MoviesLocalDto(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "overview")
    val overview: String?,
    @ColumnInfo(name = "poster_path")
    val posterPath: String?,
    @ColumnInfo(name = "release_year")
    val releaseYear: Int?,
    @ColumnInfo(name = "genres")
    val genres: String?,
    @ColumnInfo(name = "imdb_rating")
    val imdbRating: Float?
) 