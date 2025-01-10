package com.ant.models.entities

enum class VideoType(val genreValue: String) {
    TRAILER("Trailer"),
    TEASER("Teaser"),
    CLIP("Clip"),
    FEATURETTE("Featurette"),
    OPENING_CREDITS("Opening Credits");

    companion object {
        private val values by lazy { values() }
        fun fromValue(value: String) = values.firstOrNull { it.genreValue == value }
    }
}
