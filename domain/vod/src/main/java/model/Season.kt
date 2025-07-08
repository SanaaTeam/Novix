package model

data class Season(
    val id: String,
    val title: String,
    val number: Int,
    val episodes: List<Episode>
)