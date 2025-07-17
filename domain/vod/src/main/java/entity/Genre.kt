package entity

enum class Genre(val value: String) {
    ACTION("Action"),
    ADVENTURE("Adventure"),
    COMEDY("Comedy"),
    DRAMA("Drama"),
    HORROR("Horror"),
    SCIENCE_FICTION("Science Fiction"),
    FANTASY("Fantasy"),
    ROMANCE("Romance"),
    THRILLER("Thriller"),
    DOCUMENTARY("Documentary"),
    ANIMATION("Animation"),
    CRIME("Crime"),
    FAMILY("Family"),
    HISTORY("History"),
    KIDS("Kids"),
    MYSTERY("Mystery"),
    MUSIC("Music"),
    NEWS("News"),
    REALITY("Reality"),
    SOAP("Soap"),
    TALK("Talk"),
    WAR("War"),
    WAR_AND_POLITICS("War & Politics"),
    WESTERN("Western"),
    TV_MOVIE("TV Movie"),
    ACTION_AND_ADVENTURE("Action & Adventure"),
    SCI_FI_AND_FANTASY("Sci-Fi & Fantasy");
    companion object {
        fun fromValue(value: String): Genre? =
            entries.find { it.value.equals(value, ignoreCase = true) }
    }
}

