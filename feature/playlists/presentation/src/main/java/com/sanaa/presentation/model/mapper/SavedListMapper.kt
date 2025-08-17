package com.sanaa.presentation.model.mapper

import com.sanaa.presentation.bottomsheets.removeFromListBottomSheet.PlaylistUiItem
import usecase.custom_list.custom_list_param.SavedList

fun SavedList.toState(): PlaylistUiItem {
    return PlaylistUiItem(title = title, itemCount = itemCount, id = id, itemsIds = itemsIds)
}

fun List<SavedList>.toState(): List<PlaylistUiItem> {
    return map { it.toState() }
}