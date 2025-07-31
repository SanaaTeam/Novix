package com.sanaa.vod.mapper.media

import com.sanaa.vod.dataSource.remote.dto.WatchlistRequestBody
import entity.WatchlistInfo


fun WatchlistInfo.toDomain(): WatchlistRequestBody {
    return WatchlistRequestBody(
        mediaType = mediaType,
        mediaId = mediaId,
        watchlist = watchlist
    )
}

fun WatchlistInfo.toRequestBody(): WatchlistRequestBody =
    WatchlistRequestBody(mediaType, mediaId, watchlist)

