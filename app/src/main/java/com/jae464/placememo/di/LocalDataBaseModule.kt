package com.jae464.placememo.di

import android.content.Context
import androidx.room.Room
import com.jae464.data.db.FolderDao
import com.jae464.data.db.MemoDao
import com.jae464.data.db.MemoDatabase
import com.jae464.data.manager.ImageManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class LocalDataBaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MemoDatabase {
        return Room.databaseBuilder(
            context,
            MemoDatabase::class.java, "Memo.db"
        )
            .allowMainThreadQueries()
            .addCallback(MemoDatabase.callback)
            .build()
    }

    @Provides
    @Singleton
    fun provideMemoDao(memoDatabase: MemoDatabase): MemoDao {
        return memoDatabase.memoDao()
    }

    @Provides
    @Singleton
    fun provideFolderDao(memoDatabase: MemoDatabase): FolderDao {
        return memoDatabase.folderDao()
    }

    @Provides
    @Singleton
    fun provideImageManager(
        @ApplicationContext context: Context
    ): ImageManager = ImageManager(context)

}