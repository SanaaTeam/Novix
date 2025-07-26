package com.sanaa.vod.dataSource.local.search.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.sanaa.vod.util.TimeUtils

@Entity(
    tableName = "movie", primaryKeys = ["id", "language"],
)
data class MovieLocalDto(
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "image_path")
    val imagePath: String?,
    @ColumnInfo(name = "release_date")
    val releaseDate: String?,
    @ColumnInfo(name = "genres")
    val genres: String?,
    @ColumnInfo(name = "imdb_rating")
    val imdbRating: Float?,
    @ColumnInfo(name = "language")
    val language: String,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long = TimeUtils.getCurrentTimeStamp()
)