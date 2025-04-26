package com.example.newsclient.data.repository.datasourceimpl

import com.example.newsclient.data.db.ArticleDAO
import com.example.newsclient.data.model.Article
import com.example.newsclient.data.repository.datasource.NewsLocalDataSource
import kotlinx.coroutines.flow.Flow

class NewsLocalDataSourceImpl(
    private val articleDAO: ArticleDAO
): NewsLocalDataSource {
    override suspend fun saveArticleToDB(article: Article) {
        articleDAO.insert(article)
    }

    override fun getSavedArticles(): Flow<List<Article>> {
        return articleDAO.getAllArticles()
    }

    override suspend fun deleteArticle(article: Article) {
        articleDAO.deleteArticle(article)
    }
}