package entity

data class Actor(
    val id: Long,
    val imageUrl: String,
    val name: String,
    val age: Int?,
    val region: String?,
    val lastShow: String?,
    val gender: Gender
) {
    enum class Gender {
        MALE,
        FEMALE,
    }
}