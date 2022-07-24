package com.jae464.placememo.data.di

import com.jae464.placememo.data.repository.post.MemoRepositoryImpl
import com.jae464.placememo.data.repository.post.local.MemoLocalDataSource
import com.jae464.placememo.domain.repository.MemoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun provideMemoRepository(
        memoLocalDataSource: MemoLocalDataSource
    ): MemoRepository {
        return MemoRepositoryImpl(memoLocalDataSource)
    }
}