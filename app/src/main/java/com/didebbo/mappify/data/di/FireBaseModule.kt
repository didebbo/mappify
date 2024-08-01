package com.didebbo.mappify.data.di

import com.didebbo.mappify.data.provider.FirebaseDataProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class FireBaseModule {
    @Provides
    fun provideFirebaseDataProvider(): FirebaseDataProvider {
        return FirebaseDataProvider()
    }
}