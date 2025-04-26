package com.example.newsclient.domain.repository

import com.example.newsclient.data.model.APIResponse
import com.example.newsclient.data.model.Article
import com.example.newsclient.data.util.Resource
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    //Network operations
    suspend fun getNewsHeadlines(countryCode: String, page: Int): Resource<APIResponse>

    suspend fun getSearchedNews(countryCode: String, searchQuery: String, page: Int): Resource<APIResponse>

    //Local DB operations
    suspend fun saveNews(article: Article)

    suspend fun deleteNews(article: Article)

    fun getSavedNews(): Flow<List<Article>>
}