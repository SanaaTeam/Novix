package com.sanaa.vod.dataSource.local.cache.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie")
class MovieLocalDto (
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "poster_image_url")
    val posterImageUrl: String,
    @ColumnInfo(name = "release_date")
    val releaseDate: String? = null,
    @ColumnInfo(name = "rating")
    val imdbRating: Float,
)