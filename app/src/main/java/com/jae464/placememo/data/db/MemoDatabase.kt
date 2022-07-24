package com.jae464.placememo.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jae464.placememo.data.model.MemoEntity

@Database(entities = [MemoEntity::class], version = 1, exportSchema = false)
abstract class MemoDatabase: RoomDatabase() {
    abstract fun memoDao(): MemoDao
}