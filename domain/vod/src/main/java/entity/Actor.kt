package entity

data class Actor(
    val id: String,
    val imageUrl: String,
    val name: String,
    val age: Int?,
    val region: String?,
    val lastShow: String?
)
