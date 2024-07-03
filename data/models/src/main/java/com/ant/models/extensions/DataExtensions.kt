package com.ant.models.extensions

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

private const val REGEX_DATE_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy"
private const val REGEX_DATE_FORMAT_OUTPUT = "dd-MM-yyyy"


@SuppressLint("SimpleDateFormat")
fun Date.toDateString(useDifferentFormat: Boolean = false): String? {
    return this.let {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val formatter = DateTimeFormatter.ofPattern(REGEX_DATE_FORMAT, Locale.getDefault())
                val zonedDateTime = ZonedDateTime.parse(it.toString(), formatter)
                val dateTimeFormatterOutput: DateTimeFormatter = if (useDifferentFormat) {
                    DateTimeFormatter.ofPattern(REGEX_DATE_FORMAT_OUTPUT)
                } else {
                    DateTimeFormatter.ofPattern(REGEX_DATE_FORMAT)
                }
                zonedDateTime.format(dateTimeFormatterOutput)
            } else {
                val calendar = Calendar.getInstance()
                calendar.time = it
                val outputFormat = SimpleDateFormat(REGEX_DATE_FORMAT_OUTPUT, Locale.getDefault())
                outputFormat.format(calendar.time)
            }
        } catch (e: Exception) {
            null
        }
    }
}

@SuppressLint("SimpleDateFormat")
fun String.toDate(): Date? {
    return this.let {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val formatter = DateTimeFormatter.ofPattern(REGEX_DATE_FORMAT)
                val dateTime = ZonedDateTime.parse(it, formatter)
                Date.from(dateTime.toInstant())
            } else {
                val parser = SimpleDateFormat(REGEX_DATE_FORMAT, Locale.getDefault())
                parser.parse(it)
            }
        } catch (e: Exception) {
            null
        }
    }
}