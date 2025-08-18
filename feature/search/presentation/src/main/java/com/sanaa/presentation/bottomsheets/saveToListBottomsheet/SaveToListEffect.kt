package com.sanaa.presentation.bottomsheets.saveToListBottomsheet

sealed interface SaveToListEffect {
    data object CreateNewList:SaveToListEffect
    data object DismissBottomSheet:SaveToListEffect
}