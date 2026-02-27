package com.ant.tmdb.old

class TmdbImageUrlProvider {
    fun getUrl(
        path: String,
        imageSizes: String
    ): String {
        return ImageUrlProvider(imageSizes, path).imageUrl()
    }
}
