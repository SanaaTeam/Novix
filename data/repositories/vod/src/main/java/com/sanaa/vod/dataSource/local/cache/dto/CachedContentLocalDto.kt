package com.sanaa.vod.dataSource.local.cache.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "cached_content",
    primaryKeys = ["id", "metadata_id"],
    foreignKeys = [
        ForeignKey(
            entity = CachedContentMetadataLocalDto::class,
            parentColumns = ["id"],
            childColumns = ["metadata_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)

data class CachedContentLocalDto(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "poster_image_url")
    val posterImageUrl: String,
    @ColumnInfo(name = "media_type")
    val mediaType: String,
    @ColumnInfo(name = "genres")
    val genres: String,
    @ColumnInfo(name = "release_date")
    val releaseDate: String? = null,
    @ColumnInfo(name = "rating")
    val imdbRating: Float,
    @ColumnInfo(name = "metadata_id")
    val metadataId: Int,
) {
    enum class MediaType {
        MOVIE,
        TV_SHOW,
    }
}