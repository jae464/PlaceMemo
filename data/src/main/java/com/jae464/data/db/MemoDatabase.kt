package com.jae464.data.db


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jae464.data.model.CategoryEntity
import com.jae464.data.model.FolderEntity
import com.jae464.data.model.FolderMemoXRef
import com.jae464.data.model.MemoEntity
import java.util.concurrent.Executors

@Database(
    entities = [MemoEntity::class, FolderEntity::class, FolderMemoXRef::class, CategoryEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(RoomConverters::class)
abstract class MemoDatabase: RoomDatabase() {
    abstract fun memoDao(): MemoDao
    abstract fun folderDao(): FolderDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        val callback = object: Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                Executors.newSingleThreadExecutor().execute {
                    db.execSQL("INSERT INTO category (category_name) VALUES ('음식점')")
                    db.execSQL("INSERT INTO category (category_name) VALUES ('관광지')")
                    db.execSQL("INSERT INTO category (category_name) VALUES ('카페')")
                    db.execSQL("INSERT INTO category (category_name) VALUES ('호텔')")
                    db.execSQL("INSERT INTO category (category_name) VALUES ('기타')")
                }
            }
        }
    }
}