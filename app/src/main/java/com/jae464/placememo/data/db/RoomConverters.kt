package com.jae464.placememo.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import java.util.*

class RoomConverters {
    @TypeConverter
    fun imageListToJson(value: List<String>) = Gson().toJson(value)

    @TypeConverter
    fun jsonToStringList(value: String) = Gson().fromJson(value, Array<String>::class.java).toList()

    @TypeConverter
    fun dateToLong(value: Date) = value.time

    @TypeConverter
    fun longToDate(value: Long) = Date(value)
}