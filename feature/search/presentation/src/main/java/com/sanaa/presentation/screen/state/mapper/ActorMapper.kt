package com.sanaa.presentation.screen.state.mapper

import com.sanaa.presentation.screen.state.ActorUiModel
import usecase.search.search_param.SearchActorOutput

fun SearchActorOutput.toUiState(): ActorUiModel {
    return ActorUiModel(
        id= this.id,
        name = this.name,
        imageUrl = this.profileImageUrl
    )
}