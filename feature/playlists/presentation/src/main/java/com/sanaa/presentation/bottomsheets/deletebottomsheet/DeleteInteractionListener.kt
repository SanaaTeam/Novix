package com.sanaa.presentation.bottomsheets.deletebottomsheet

interface DeleteInteractionListener {
    fun onDeleteConfirmed(listId: Long)
    fun onSnackBarDismiss()
}