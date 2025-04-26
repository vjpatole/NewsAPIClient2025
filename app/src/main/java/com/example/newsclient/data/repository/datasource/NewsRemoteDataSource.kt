package com.example.newsclient.data.repository.datasource

import com.example.newsclient.data.model.APIResponse
import retrofit2.Response

interface NewsRemoteDataSource {
    suspend fun getTopHeadLines(countryCode: String, page: Int): Response<APIResponse>

    suspend fun getSearchedNews(countryCode: String, searchQuery: String, page: Int): Response<APIResponse>
}