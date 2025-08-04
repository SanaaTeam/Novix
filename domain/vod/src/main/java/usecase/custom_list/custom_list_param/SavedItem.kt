package usecase.custom_list.custom_list_param

import usecase.search.search_param.MediaType

data class SavedItem(
    val id: Int,
    val posterImageUrl: String,
    val mediaType: MediaType
)