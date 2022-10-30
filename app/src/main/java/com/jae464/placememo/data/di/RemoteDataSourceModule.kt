package com.jae464.placememo.data.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jae464.placememo.data.repository.login.remote.LoginRemoteDataSource
import com.jae464.placememo.data.repository.login.remote.LoginRemoteDataSourceImpl
import com.jae464.placememo.data.repository.memo.remote.MemoRemoteDataSource
import com.jae464.placememo.data.repository.memo.remote.MemoRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

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

//    @Singleton
//    @Provides
//    fun provideFireStore(): FirebaseFirestore {
//        return Firebase.firestore
//    }
//
//    @Singleton
//    @Provides
//    fun provideMemoRemoteDataSource(firestore: FirebaseFirestore): MemoRemoteDataSource {
//        return MemoRemoteDataSourceImpl(firestore)
//    }
}