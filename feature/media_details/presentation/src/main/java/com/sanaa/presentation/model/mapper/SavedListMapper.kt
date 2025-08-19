package com.sanaa.presentation.model.mapper

import com.sanaa.presentation.bottomsheets.saveToListBottomsheet.PlaylistUiStateItem
import usecase.custom_list.custom_list_param.SavedList


fun SavedList.toState(): PlaylistUiStateItem {
    return PlaylistUiStateItem(title = title, itemCount = itemCount, id = id, itemsIds = itemsIds)
}

fun List<SavedList>.toState(): List<PlaylistUiStateItem> {
    return map { it.toState() }
}