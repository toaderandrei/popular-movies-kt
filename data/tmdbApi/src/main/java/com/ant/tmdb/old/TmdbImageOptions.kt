package com.ant.tmdb.old

class ImageUrlProvider(private val imageSize: String, private val path: String? = null) {

    companion object {
        const val IMAGE_URL = "https://image.tmdb.org/t/p/"
    }

    fun imageUrl(): String {
        val stringBuilder: StringBuilder = StringBuilder()
        stringBuilder.append(IMAGE_URL)
            .append(imageSize)
            .append("/")
            .append(path)
        return stringBuilder.toString()
    }
}

enum class BackdropSizes(val wSize: String) {
    W300("w300"),
    W780("w780"),
    W1280("w1280"),
    ORIGINAL("original"),
}

enum class LogoSizes(val wSize: String) {
    W45("w45"),
    W92("w92"),
    W154("w154"),
    W185("w185"),
    W300("w300"),
    W500("w500"),
    ORIGINAL("original")
}

enum class PosterSizes(val wSize: String) {
    W92("w92"),
    W154("w154"),
    W185("w185"),
    W342("w342"),
    W500("w500"),
    W780("w780"),
    ORIGINAL("original")
}