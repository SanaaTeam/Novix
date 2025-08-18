package com.sanaa.presentation.screen.state.mapper

import com.sanaa.presentation.bottomsheets.saveToListBottomsheet.PlaylistUiItem
import usecase.custom_list.custom_list_param.SavedList

fun SavedList.toState(): PlaylistUiItem {
    return PlaylistUiItem(title = title, itemCount = itemCount, id = id, itemsIds = itemsIds)
}

fun List<SavedList>.toState(): List<PlaylistUiItem> {
    return map { it.toState() }
}