package com.example.newsclient.ui.di

import android.app.Application
import com.example.newsclient.domain.usecase.DeleteSavedNewsUseCase
import com.example.newsclient.domain.usecase.GetNewsHeadlinesUseCase
import com.example.newsclient.domain.usecase.GetSavedNewsUseCase
import com.example.newsclient.domain.usecase.GetSearchedNewsUseCase
import com.example.newsclient.domain.usecase.SaveNewsUseCase
import com.example.newsclient.ui.viewmodel.NewsViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FactoryModule {
    @Singleton
    @Provides
    fun provideNewsViewModelFactory(
        application: Application,
        getNewsHeadlinesUseCase: GetNewsHeadlinesUseCase,
        getSearchedNewsUseCase: GetSearchedNewsUseCase,
        saveNewsUseCase: SaveNewsUseCase,
        getSaveNewsUseCase: GetSavedNewsUseCase,
        deleteSavedNewsUseCase: DeleteSavedNewsUseCase
    ): NewsViewModelFactory {
        return NewsViewModelFactory(application, getNewsHeadlinesUseCase, getSearchedNewsUseCase, saveNewsUseCase, getSaveNewsUseCase, deleteSavedNewsUseCase)
    }
}