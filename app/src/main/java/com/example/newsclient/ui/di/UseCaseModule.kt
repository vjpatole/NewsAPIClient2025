package com.example.newsclient.ui.di

import com.example.newsclient.domain.repository.NewsRepository
import com.example.newsclient.domain.usecase.DeleteSavedNewsUseCase
import com.example.newsclient.domain.usecase.GetNewsHeadlinesUseCase
import com.example.newsclient.domain.usecase.GetSavedNewsUseCase
import com.example.newsclient.domain.usecase.GetSearchedNewsUseCase
import com.example.newsclient.domain.usecase.SaveNewsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Singleton
    @Provides
    fun provideNewsHeadlinesUseCase(newsRepository: NewsRepository): GetNewsHeadlinesUseCase{
        return GetNewsHeadlinesUseCase(newsRepository)
    }

    @Singleton
    @Provides
    fun provideSearchedNewsUseCase(newsRepository: NewsRepository): GetSearchedNewsUseCase{
        return GetSearchedNewsUseCase(newsRepository)
    }

    @Singleton
    @Provides
    fun provideSaveNewsUseCase(newsRepository: NewsRepository): SaveNewsUseCase{
        return SaveNewsUseCase(newsRepository)
    }

    @Singleton
    @Provides
    fun provideGetSavedNewsUseCase(newsRepository: NewsRepository): GetSavedNewsUseCase{
        return GetSavedNewsUseCase(newsRepository)
    }

    @Singleton
    @Provides
    fun provideDeleteSavedNewsUseCase(newsRepository: NewsRepository): DeleteSavedNewsUseCase{
        return DeleteSavedNewsUseCase(newsRepository)
    }
}