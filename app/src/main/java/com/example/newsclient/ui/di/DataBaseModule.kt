package com.example.newsclient.ui.di

import android.app.Application
import androidx.room.Room
import com.example.newsclient.data.db.ArticleDAO
import com.example.newsclient.data.db.ArticleDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {

    @Singleton
    @Provides
    fun provideNewsDatabase(app: Application): ArticleDatabase{
        return Room.databaseBuilder(app, ArticleDatabase::class.java, "NEWS_DB")
            .fallbackToDestructiveMigration(false)
            .build()
    }

    @Singleton
    @Provides
    fun provideNewsDao(articleDatabase: ArticleDatabase): ArticleDAO{
        return articleDatabase.getArticleDao()
    }
}