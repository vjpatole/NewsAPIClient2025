package com.example.newsclient.data.repository.datasourceimpl

import com.example.newsclient.data.api.NewsAPIService
import com.example.newsclient.data.model.APIResponse
import com.example.newsclient.data.repository.datasource.NewsRemoteDataSource
import retrofit2.Response

class NewsRemoteDataSourceImpl(
    private val newsAPIService: NewsAPIService): NewsRemoteDataSource {

    override suspend fun getTopHeadLines(countryCode: String, page: Int): Response<APIResponse> {
        return newsAPIService.getTopHeadLines(countryCode, page)
    }

    override suspend fun getSearchedNews(
        countryCode: String,
        searchQuery: String,
        page: Int
    ): Response<APIResponse> {
        return newsAPIService.getSearchedTopHeadLines(countryCode, searchQuery, page)
    }
}