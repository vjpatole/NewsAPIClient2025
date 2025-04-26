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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewsViewModel(
    private val appContext: Application,
    private val getNewsHeadlinesUseCase: GetNewsHeadlinesUseCase,
    private val getSearchedNewsUseCase: GetSearchedNewsUseCase,
    private val saveNewsUseCase: SaveNewsUseCase,
    private val getSaveNewsUseCase: GetSavedNewsUseCase,
    private val deleteSavedNewsUseCase: DeleteSavedNewsUseCase
): AndroidViewModel(appContext) {

    val newsHeadLines: MutableLiveData<Resource<APIResponse>> = MutableLiveData()
    val searchedNewsList: MutableLiveData<Resource<APIResponse>> = MutableLiveData()

    fun getNewsHeadLines(country: String, page: Int){
        viewModelScope.launch(Dispatchers.IO) {
            newsHeadLines.postValue(Resource.Loading())
            try {
                if (isNetworkAvailable(appContext)) {
                    val apiResult = getNewsHeadlinesUseCase.execute(country, page)
                    newsHeadLines.postValue(apiResult)
                } else {
                    newsHeadLines.postValue(Resource.Error("Internet not available!!!"))
                }
            }catch (e: Exception){
                newsHeadLines.postValue(Resource.Error(e.message.toString()))
            }
        }
    }

    //Search
    fun getSearchedNews(country: String, searchedQuery: String, page: Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (isNetworkAvailable(appContext)){
                    val apiResult = getSearchedNewsUseCase.execute(country, searchedQuery, page)
                    searchedNewsList.postValue(apiResult)
                }else {
                    searchedNewsList.postValue(Resource.Error("Internet not available!!!"))
                }
            }catch (e: Exception){
                searchedNewsList.postValue(Resource.Error(e.message.toString()))
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