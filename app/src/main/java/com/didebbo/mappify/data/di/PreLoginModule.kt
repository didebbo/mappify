package com.didebbo.mappify.data.di

import com.didebbo.mappify.data.provider.FirebaseDataProvider
import com.didebbo.mappify.domain.repository.PreLoginRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class PreLoginModule {
    @Provides
    fun providePreLoginRepository(firebaseDataProvider: FirebaseDataProvider): PreLoginRepository {
        return PreLoginRepository(firebaseDataProvider)
    }
}