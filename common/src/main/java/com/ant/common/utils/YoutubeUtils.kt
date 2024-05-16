package com.ant.common.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri

object YoutubeUtils {
    fun getQuality(movieId: String, quality: Quality): String {
        return when (quality) {
            Quality.MAX -> {
                //  Size : 1920x1080
                //  May not be available
                "https://img.youtube.com/vi/$movieId/maxresdefault.jpg"
            }
            Quality.HIGH -> {
                //  Size : 480x360
                "https://img.youtube.com/vi/$movieId/hqdefault.jpg"
            }
            Quality.MEDIUM -> {
                //  Size : 320x180
                "https://img.youtube.com/vi/$movieId/mqdefault.jpg"
            }
            else -> {
                //  Size : 120x90
                "https://img.youtube.com/vi/$movieId/default.jpg"
            }
        }
    }

    fun launchYoutube(context: Context, videoId: String?) {
        if (videoId == null) {
            throw IllegalArgumentException("Video id should not be null")
        }

        val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$videoId"))
        val webIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://www.youtube.com/watch?v=$videoId")
        )
        try {
            context.startActivity(appIntent)
        } catch (ex: ActivityNotFoundException) {
            context.startActivity(webIntent)
        }
    }

    enum class Quality {
        MAX,
        HIGH,
        MEDIUM,
        NORMAL
    }
}
