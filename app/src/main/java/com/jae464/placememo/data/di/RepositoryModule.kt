package com.jae464.placememo.data.di

import com.jae464.placememo.data.api.RetrofitClient
import com.jae464.placememo.data.repository.address.AddressRepositoryImpl
import com.jae464.placememo.data.repository.login.LoginRepositoryImpl
import com.jae464.placememo.data.repository.login.remote.LoginRemoteDataSource
import com.jae464.placememo.data.repository.memo.MemoRepositoryImpl
import com.jae464.placememo.data.repository.memo.local.MemoLocalDataSource
import com.jae464.placememo.domain.repository.AddressRepository
import com.jae464.placememo.domain.repository.LoginRepository
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

    @Provides
    @Singleton
    fun provideAddressRepository(): AddressRepository {
        return AddressRepositoryImpl(RetrofitClient.geoService)
    }

    @Provides
    @Singleton
    fun provideLoginRepository(
        loginRemoteDataSource: LoginRemoteDataSource
    ): LoginRepository {
        return LoginRepositoryImpl(loginRemoteDataSource)
    }
}