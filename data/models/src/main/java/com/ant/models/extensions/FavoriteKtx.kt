package com.ant.models.extensions

import com.ant.models.request.FavoriteType
import com.uwetrottmann.tmdb2.enumerations.MediaType

fun FavoriteType.toMediaType(): MediaType {
    return when (this) {
        FavoriteType.TV -> MediaType.TV
        FavoriteType.MOVIE -> MediaType.MOVIE
        FavoriteType.PERSON -> MediaType.PERSON

    }
}