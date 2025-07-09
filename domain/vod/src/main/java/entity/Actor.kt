package entity

data class Actor(
    val id: Long,
    val imageUrl: String,
    val name: String,
    val age: Int?,
    val region: String?,
    val lastShow: String?
)
