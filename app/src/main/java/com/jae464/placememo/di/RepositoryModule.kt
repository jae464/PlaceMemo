package com.jae464.placememo.di

import com.jae464.data.repository.address.AddressRepositoryImpl
import com.jae464.data.repository.login.LoginRepositoryImpl
import com.jae464.data.repository.memo.MemoRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
interface RepositoryModule {

    @Binds
    fun bindMemoRepository(
        memoRepositoryImpl: MemoRepositoryImpl
    ): com.jae464.domain.repository.MemoRepository

    @Binds
    fun bindAddressRepository(
        addressRepositoryImpl: AddressRepositoryImpl
    ): com.jae464.domain.repository.AddressRepository

    @Binds
    fun bindLoginRepository(
        loginRepositoryImpl: LoginRepositoryImpl
    ): com.jae464.domain.repository.LoginRepository

}