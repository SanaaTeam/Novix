package com.sanaa.presentation.screen.state.mapper

import com.sanaa.tvapp.presentation.screens.searchScreen.ActorUiModel
import entity.Actor

fun Actor.toUiState(): ActorUiModel {
    return ActorUiModel(
        id = this.id,
        name = this.name,
        imageUrl = this.imageUrl
    )
}