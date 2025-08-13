package entity

data class Season(
    val id: Int,
    val title: String,
    val overview: String,
    val number: Int,
    val episodes: List<Episode>,
)