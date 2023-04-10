package com.app.procom.storage.movies.converters

import androidx.room.TypeConverter

class ListStringConverter {
    @TypeConverter
    fun fromListString(list: List<String>): String {
        return list.joinToString(",")
    }

    @TypeConverter
    fun toListString(string: String): List<String> {
        return string.split(",")
    }
}