package com.sanaa.presentation.bottomsheets.saveToListBottomsheet

sealed interface SaveToListBottomSheetEffect {
    data object CreateNewList:SaveToListBottomSheetEffect
    data object DismissBottomSheet:SaveToListBottomSheetEffect
}