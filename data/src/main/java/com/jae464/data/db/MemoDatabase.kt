package com.jae464.data.db


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jae464.data.model.MemoEntity

@Database(entities = [MemoEntity::class], version = 1, exportSchema = false)
@TypeConverters(RoomConverters::class)
abstract class MemoDatabase: RoomDatabase() {
    abstract fun memoDao(): MemoDao
}