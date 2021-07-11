package com.androiddevs.mvvmnewsapp.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.*
import com.androiddevs.mvvmnewsapp.NewsApplication
import com.androiddevs.mvvmnewsapp.models.NewsResponse
import com.androiddevs.mvvmnewsapp.repository.NewsRepository
import com.androiddevs.mvvmnewsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class SearchNewsViewModel(
    application: Application,
    private val repository : NewsRepository) : AndroidViewModel(application) {

    private val _searchNews = MutableLiveData<Resource<NewsResponse>>()
    val searchNews : LiveData<Resource<NewsResponse>>
        get() = _searchNews

    var searchNewsPage = 1
    private var searchNewsResponse : NewsResponse? = null

    var preQuery : String? = null

    private suspend fun safeSearchNewsCall(searchQuery: String){
        _searchNews.postValue(Resource.Loading())
        try{
            if(hasInternetConnection()){

                if(preQuery != searchQuery)
                    searchNewsPage=1

                Log.i("SearchNewsViewModel","pageSize - $searchNewsPage")

                val response = repository.searchNews(searchQuery   ,searchNewsPage)
                _searchNews.postValue(handleSearchNewsResponse(response))
            }
            else{
                _searchNews.postValue(Resource.Error("No Internet Connection"))
            }
        }
        catch (t : Throwable){
            when(t){
                is IOException -> _searchNews.postValue(Resource.Error("Network Failure."))
                else -> _searchNews.postValue(Resource.Error("Conversion Error."))
            }
        }
    }

    fun searchNews(searchQuery : String){
        viewModelScope.launch {
            safeSearchNewsCall(searchQuery)
        }
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->

                searchNewsPage++
                if(searchNewsPage==2){
                    searchNewsResponse=resultResponse
                } else{
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = resultResponse.articles

                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(searchNewsResponse?:resultResponse)
            }
        }

        Log.i("Error","Response not successfull")
        return Resource.Error(response.message())
    }

    private fun hasInternetConnection() : Boolean{
        val connectivityManager = getApplication<NewsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

            return when{
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else{
            connectivityManager.activeNetworkInfo?.run {
                return when(type){
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET ->  true
                    else -> false
                }
            }
        }
        return false
    }

}