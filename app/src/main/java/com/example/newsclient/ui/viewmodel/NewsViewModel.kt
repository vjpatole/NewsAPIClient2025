package com.example.newsclient.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.newsclient.data.model.APIResponse
import com.example.newsclient.data.model.Article
import com.example.newsclient.data.util.Resource
import com.example.newsclient.domain.usecase.DeleteSavedNewsUseCase
import com.example.newsclient.domain.usecase.GetNewsHeadlinesUseCase
import com.example.newsclient.domain.usecase.GetSavedNewsUseCase
import com.example.newsclient.domain.usecase.GetSearchedNewsUseCase
import com.example.newsclient.domain.usecase.SaveNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewsViewModel(
    private val appContext: Application,
    private val getNewsHeadlinesUseCase: GetNewsHeadlinesUseCase,
    private val getSearchedNewsUseCase: GetSearchedNewsUseCase,
    private val saveNewsUseCase: SaveNewsUseCase,
    private val getSaveNewsUseCase: GetSavedNewsUseCase,
    private val deleteSavedNewsUseCase: DeleteSavedNewsUseCase
): AndroidViewModel(appContext) {

    private val _newsHeadlines = MutableStateFlow<Resource<APIResponse>>(Resource.Loading())
    val newsHeadlines: StateFlow<Resource<APIResponse>> = _newsHeadlines

    private val _searchedNewsList = MutableStateFlow<Resource<APIResponse>>(Resource.Loading())
    val searchedNewsList: StateFlow<Resource<APIResponse>> = _searchedNewsList


    fun getNewsHeadlines(country: String, page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _newsHeadlines.value = Resource.Loading()
            try {
                if (isNetworkAvailable(appContext)) {
                    val apiResult = getNewsHeadlinesUseCase.execute(country, page)
                    _newsHeadlines.value = apiResult
                } else {
                    _newsHeadlines.value = Resource.Error("Internet not available!!!")
                }
            } catch (e: Exception) {
                _newsHeadlines.value = Resource.Error(e.message.toString())
            }
        }
    }

    fun getSearchedNews(country: String, searchedQuery: String, page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (isNetworkAvailable(appContext)) {
                    val apiResult = getSearchedNewsUseCase.execute(country, searchedQuery, page)
                    _searchedNewsList.value = apiResult
                } else {
                    _searchedNewsList.value = Resource.Error("Internet not available!!!")
                }
            } catch (e: Exception) {
                _searchedNewsList.value = Resource.Error(e.message.toString())
            }
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun isNetworkAvailable(context: Context): Boolean{
        var result = false
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm?.run {
                cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                    result = when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                        else -> false
                    }
                }
            }
        } else {
            cm?.run {
                cm.activeNetworkInfo?.run {
                    if (type == ConnectivityManager.TYPE_WIFI) {
                        result = true
                    } else if (type == ConnectivityManager.TYPE_MOBILE) {
                        result = true
                    }
                }
            }
        }
        return result
    }

    //local Data
    fun saveArticle(article: Article) = viewModelScope.launch {
        saveNewsUseCase.execute(article)
    }

    fun getSavedNews()= liveData {
        getSaveNewsUseCase.execute().collect{
            emit(it)
        }
    }

    fun deleteArticles(article: Article) = viewModelScope.launch{
        deleteSavedNewsUseCase.execute(article)
    }
}