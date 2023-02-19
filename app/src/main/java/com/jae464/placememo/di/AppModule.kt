package com.jae464.placememo.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jae464.data.api.GeoService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideGeoService(): GeoService {
        return com.jae464.data.api.RetrofitClient.geoService
    }

    @Singleton
    @Provides
    fun provideFireStore(): FirebaseFirestore {
        return Firebase.firestore
    }

}