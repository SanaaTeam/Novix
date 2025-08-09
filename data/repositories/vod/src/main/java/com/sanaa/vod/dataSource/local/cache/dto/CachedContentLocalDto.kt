package com.sanaa.vod.dataSource.local.cache.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "cached_content",
    primaryKeys = ["id", "item_id", "content_type"],
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
    @ColumnInfo(name = "item_id")
    val itemId: Int,
    @ColumnInfo(name = "content_type")
    val contentType: String,
) {
    enum class ContentType {
        MOVIE,
        TV_SHOW,
        GENRE,
    }
}