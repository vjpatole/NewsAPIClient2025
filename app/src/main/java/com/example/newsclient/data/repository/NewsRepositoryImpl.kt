package com.example.newsclient.data.repository

import com.example.newsclient.data.model.APIResponse
import com.example.newsclient.data.model.Article
import com.example.newsclient.data.repository.datasource.NewsLocalDataSource
import com.example.newsclient.data.repository.datasource.NewsRemoteDataSource
import com.example.newsclient.domain.repository.NewsRepository
import com.example.newsclient.data.util.Resource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class NewsRepositoryImpl(
    private val newsRemoteDataSource: NewsRemoteDataSource,
    private val newsLocalDataSource: NewsLocalDataSource
): NewsRepository {

    override suspend fun getNewsHeadlines(countryCode: String, page: Int): Resource<APIResponse> {
        return responseToResource(newsRemoteDataSource.getTopHeadLines(countryCode, page))
    }

    override suspend fun getSearchedNews(
        countryCode: String,
        searchQuery: String,
        page: Int
    ): Resource<APIResponse> {
        return responseToResource(newsRemoteDataSource.getSearchedNews(countryCode, searchQuery, page))
    }

    private fun responseToResource(response: Response<APIResponse>): Resource<APIResponse>{
        if (response.isSuccessful){
            response.body()?.let {result ->
                return Resource.Success(result)
            }
        }

        return Resource.Error(response.message())
    }

    override suspend fun saveNews(article: Article) {
        newsLocalDataSource.saveArticleToDB(article)
    }

    override suspend fun deleteNews(article: Article) {
        newsLocalDataSource.deleteArticle(article)
    }

    override fun getSavedNews(): Flow<List<Article>> {
        return newsLocalDataSource.getSavedArticles()
    }
}