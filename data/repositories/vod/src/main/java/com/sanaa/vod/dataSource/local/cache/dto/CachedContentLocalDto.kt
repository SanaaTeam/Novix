package com.sanaa.vod.dataSource.local.cache.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "cached_content",
    primaryKeys = ["id", "media_type", "media_id"],
    foreignKeys = [
        ForeignKey(
            entity = CachedContentMetadataLocalDto::class,
            parentColumns = ["id"],
            childColumns = ["id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CachedContentLocalDto(
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "media_id")
    val mediaId: Int,
    @ColumnInfo(name = "media_type")
    val mediaType: String,
) {
    enum class MediaType {
        MOVIE,
        TV_SHOW,
    }
}