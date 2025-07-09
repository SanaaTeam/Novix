package entity

enum class Language(val code: String) {
    ENGLISH("en"),
    ARABIC("ar");

    companion object {
        fun fromCode(code: String): Language? =
            Language.entries.find { it.code == code }
    }
}