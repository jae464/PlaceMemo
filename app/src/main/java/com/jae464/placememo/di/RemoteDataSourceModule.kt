package com.jae464.placememo.di

import com.jae464.data.repository.login.remote.LoginRemoteDataSource
import com.jae464.data.repository.login.remote.LoginRemoteDataSourceImpl
import com.jae464.data.repository.memo.remote.MemoRemoteDataSource
import com.jae464.data.repository.memo.remote.MemoRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface RemoteDataSourceModule {

    @Binds
    fun bindLoginRemoteDataSource(
        loginRemoteDataSourceImpl: LoginRemoteDataSourceImpl
    ): LoginRemoteDataSource

    @Binds
    fun bindMemoRemoteDataSource(
        memoRemoteDataSourceImpl: MemoRemoteDataSourceImpl
    ): MemoRemoteDataSource

}