package com.sanaa.vod.dataSource.local.search.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.sanaa.vod.util.TimeUtils

@Entity(
    tableName = "actor", primaryKeys = ["id", "language"],
)
data class ActorLocalDto(
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "image_path")
    val imagePath: String?,
    @ColumnInfo(name = "language")
    val language: String,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long = TimeUtils.getCurrentTimeStamp(),
)