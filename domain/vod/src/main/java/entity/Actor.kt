package entity

import kotlinx.datetime.LocalDate

data class Actor(
    val id: Int,
    val imageUrl: String,
    val name: String,
    val region: String?,
    val lastShow: String?,
    val gender: Gender,
    val department: String?,
    val birthDate: LocalDate?,
    val deathDate: LocalDate?,
    val placeOfBirth: String?,

) {
    enum class Gender {
        MALE,
        FEMALE,
    }
}