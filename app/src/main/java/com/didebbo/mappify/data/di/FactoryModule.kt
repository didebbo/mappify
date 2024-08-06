package com.didebbo.mappify.data.di

import com.didebbo.mappify.presentation.view.component.markerpost.infowindow.MarkerPostInfoWindowFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class FactoryModule {
    @Provides
    fun provideMarkerPostInfoWindowFactory(): MarkerPostInfoWindowFactory {
        return MarkerPostInfoWindowFactory()
    }
}