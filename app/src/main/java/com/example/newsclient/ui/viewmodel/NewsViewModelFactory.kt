package com.example.newsclient.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newsclient.domain.usecase.DeleteSavedNewsUseCase
import com.example.newsclient.domain.usecase.GetNewsHeadlinesUseCase
import com.example.newsclient.domain.usecase.GetSavedNewsUseCase
import com.example.newsclient.domain.usecase.GetSearchedNewsUseCase
import com.example.newsclient.domain.usecase.SaveNewsUseCase

class NewsViewModelFactory(
    private val appContext: Application,
    private val getNewsHeadlinesUseCase: GetNewsHeadlinesUseCase,
    private val getSearchedNewsUseCase: GetSearchedNewsUseCase,
    private val saveNewsUseCase: SaveNewsUseCase,
    private val getSavedNewsUseCase: GetSavedNewsUseCase,
    private val deleteSavedNewsUseCase: DeleteSavedNewsUseCase
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(NewsViewModel::class.java)){
            return NewsViewModel(
                appContext,
                getNewsHeadlinesUseCase,
                getSearchedNewsUseCase,
                saveNewsUseCase,
                getSavedNewsUseCase,
                deleteSavedNewsUseCase
            ) as T
        }

        return throw IllegalArgumentException("Invalid View Model supplied")
    }
}