package com.jae464.placememo.di

import com.jae464.data.repository.memo.local.MemoLocalDataSource
import com.jae464.data.repository.memo.local.MemoLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface LocalDataSourceModule {

    @Binds
    fun bindMemoLocalDataSource(
        memoLocalDataSourceImpl: MemoLocalDataSourceImpl
    ): MemoLocalDataSource

}