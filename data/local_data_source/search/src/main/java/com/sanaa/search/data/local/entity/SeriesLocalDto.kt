package com.sanaa.search.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "serie")
data class SeriesLocalDto(
    @PrimaryKey
    @ColumnInfo(name = "series_id")
    val seriesId: Int,

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