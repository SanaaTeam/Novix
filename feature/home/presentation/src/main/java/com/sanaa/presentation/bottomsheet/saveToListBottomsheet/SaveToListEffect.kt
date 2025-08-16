package com.sanaa.presentation.bottomsheet.saveToListBottomsheet

sealed interface SaveToListEffect {
    data object CreateNewList:SaveToListEffect
    data object DismissBottomSheet:SaveToListEffect
}