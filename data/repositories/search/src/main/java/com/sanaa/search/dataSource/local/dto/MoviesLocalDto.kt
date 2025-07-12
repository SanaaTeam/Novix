package com.sanaa.search.dataSource.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "movie", primaryKeys = ["id", "language"],
)
data class MoviesLocalDto(
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "image_path")
    val imagePath: String?,
    @ColumnInfo(name = "release_year")
    val releaseYear: Int?,
    @ColumnInfo(name = "genres")
    val genres: String?,
    @ColumnInfo(name = "imdb_rating")
    val imdbRating: Float?,
    @ColumnInfo(name = "language")
    val language: String,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long = System.currentTimeMillis()
)