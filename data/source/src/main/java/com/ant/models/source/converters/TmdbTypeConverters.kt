package com.ant.models.source.converters

import androidx.room.TypeConverter
import com.ant.models.extensions.toDate
import com.ant.models.extensions.toDateString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.util.Date


object TmdbTypeConverters {

    @TypeConverter
    @JvmStatic
    fun toLocalTime(value: String?) = value?.toDate()

    @TypeConverter
    @JvmStatic
    fun fromLocalTime(value: Date?) = value?.toDateString()

    @TypeConverter
    @JvmStatic
    fun fromStringToList(value: String?): List<Int> {
        if (value == null) {
            return emptyList()
        }
        return Json.decodeFromString(value)
    }

    @TypeConverter
    @JvmStatic
    fun fromArrayToList(list: List<Int>?): String? {
        if (list == null) {
            return null
        }
        return Json.encodeToString(list)
    }

    @TypeConverter
    @JvmStatic
    fun fromStringToListOfStrings(genreIds: String?): List<String> {
        if (genreIds == null) {
            return emptyList()
        }
        return Json.decodeFromString(genreIds)
    }

    @TypeConverter
    @JvmStatic
    fun fromListToString(list: List<String>?): String? {
        if (list.isNullOrEmpty()) {
            return null
        }
        return Json.encodeToString(list)
    }
}