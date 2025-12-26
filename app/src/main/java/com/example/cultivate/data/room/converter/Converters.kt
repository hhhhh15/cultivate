package com.example.cultivate.data.room.converter

import android.net.Uri
import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object Converters {
    private val formatter = DateTimeFormatter.ofPattern("HH:mm")
    private val formatterDate=DateTimeFormatter.ofPattern("yyyy-MM-dd")
    // Uri 转 String
    @TypeConverter
    fun fromUri(uri: Uri?): String? {
        return uri?.toString()
    }

    // String 转 Uri
    @TypeConverter
    fun toUri(uriString: String?): Uri? {
        return uriString?.let { Uri.parse(it) }
    }

    @TypeConverter
    fun fromLocalTime(time: LocalTime?):String?{
        return time?.format(formatter)
    }

    @TypeConverter
    fun toLocalTime(timeString:String?): LocalTime?{
        return timeString?.let { it->
            LocalTime.parse(it,formatter)
        }
    }
    @TypeConverter
    fun formLocalDate(time:LocalDate):String?{
        return time.format(formatterDate)
    }
    @TypeConverter
    fun toLocalDate(dateString:String):LocalDate{
        return dateString.let {it->
            LocalDate.parse(it,formatterDate)
        }
    }
}