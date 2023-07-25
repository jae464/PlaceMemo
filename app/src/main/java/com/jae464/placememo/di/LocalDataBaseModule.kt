package com.jae464.placememo.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.jae464.data.db.CategoryDao
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

    private val USER_PREFERENCES = "user_preferences"
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
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = {
                context.preferencesDataStoreFile(USER_PREFERENCES)
            }
        )
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
    fun provideCategoryDao(memoDatabase: MemoDatabase): CategoryDao {
        return memoDatabase.categoryDao()
    }

    @Provides
    @Singleton
    fun provideImageManager(
        @ApplicationContext context: Context
    ): ImageManager = ImageManager(context)

}