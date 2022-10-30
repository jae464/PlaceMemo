package com.jae464.placememo.data.di

import android.content.Context
import androidx.room.Room
import com.jae464.placememo.data.db.MemoDao
import com.jae464.placememo.data.db.MemoDatabase
import com.jae464.placememo.data.repository.login.remote.LoginRemoteDataSource
import com.jae464.placememo.data.repository.login.remote.LoginRemoteDataSourceImpl
import com.jae464.placememo.data.repository.memo.local.MemoLocalDataSource
import com.jae464.placememo.data.repository.memo.local.MemoLocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class LocalDataModule {

//    @Provides
//    @Singleton
//    fun provideMemoLocalDataSource(memoDao: MemoDao):MemoLocalDataSource {
//        return MemoLocalDataSourceImpl(memoDao)
//    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MemoDatabase {
        return Room.databaseBuilder(
            context,
            MemoDatabase::class.java, "Memo.db"
        )
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    @Singleton
    fun provideMemoDao(memoDatabase: MemoDatabase): MemoDao{
        return memoDatabase.memoDao()
    }

//    @Provides
//    @Singleton
//    fun provideLoginRemoteDataSource(): LoginRemoteDataSource {
//        return LoginRemoteDataSourceImpl()
//    }
}