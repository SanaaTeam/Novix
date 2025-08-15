package com.sanaa.vod.dataSource.local.cache.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "saved_lists",
)
data class SavedListLocalDto(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "list_name")
    val listName: String,
    @ColumnInfo(name = "movie_ids")
    val movieIds: String,
)

@Entity(
    tableName = "saved_movies",
)
data class SavedMovieLocalDto(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "poster_url")
    val posterUrl: String,
)





