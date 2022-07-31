package com.jae464.placememo.data.db

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import java.io.ByteArrayOutputStream
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

    @TypeConverter
    fun bitmapToByteArray(bitmap: Bitmap?): ByteArray {
        if(bitmap == null) return byteArrayOf()
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    @TypeConverter
    fun byteArrayToBitmap(bytes: ByteArray): Bitmap? {
        if (bytes.isEmpty()) return null
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }
}