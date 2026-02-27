package com.ant.models.model

enum class GenreData(val genreValue: String) {
    DRAMA("drama"),
    FANTASY("fantasy"),
    SCIENCE_FICTION("science-fiction"),
    ACTION("action"),
    ADVENTURE("adventure"),
    CRIME("crime"),
    THRILLER("thriller"),
    COMEDY("comedy"),
    HORROR("horror"),
    MYSTERY("mystery");

    companion object {
        private val values by lazy { values() }
        fun fromValue(value: String) = values.firstOrNull { it.genreValue == value }
    }
}
