package com.example.newsclient.domain.usecase

import com.example.newsclient.data.model.APIResponse
import com.example.newsclient.data.util.Resource
import com.example.newsclient.domain.repository.NewsRepository

class GetNewsHeadlinesUseCase(private val newsRepository: NewsRepository) {
    suspend fun execute(countryCode: String, page: Int): Resource<APIResponse>{
        return newsRepository.getNewsHeadlines(countryCode, page)
    }
}