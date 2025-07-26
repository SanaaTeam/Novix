package com.sanaa.presentation.state

data class PersonUiState(
    val id: Int,
    val name: String,
    val character: String? = null,
    val imageUrl: String
)