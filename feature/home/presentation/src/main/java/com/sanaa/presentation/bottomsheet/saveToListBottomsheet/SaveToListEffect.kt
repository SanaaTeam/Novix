package com.sanaa.presentation.bottomsheet.saveToListBottomsheet

sealed interface SaveToListEffect {
    object CreateNewList:SaveToListEffect
    object DismissBottomSheet:SaveToListEffect
}