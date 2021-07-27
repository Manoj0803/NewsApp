package com.androiddevs.mvvmnewsapp.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.*
import com.androiddevs.mvvmnewsapp.NewsApplication
import com.androiddevs.mvvmnewsapp.models.Article
import com.androiddevs.mvvmnewsapp.models.Country
import com.androiddevs.mvvmnewsapp.models.NewsResponse
import com.androiddevs.mvvmnewsapp.repository.NewsRepository
import com.androiddevs.mvvmnewsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

enum class ApiStatus { LOADING, ERROR, DONE }

class NewsViewModel(
    application: Application,
    private val repository : NewsRepository) : AndroidViewModel(application) {

    private val _breakingNews = MutableLiveData<Resource<NewsResponse>>()
    val breakingNews : LiveData<Resource<NewsResponse>>
        get() = _breakingNews

    var breakingNewsPage : Int = 1
    private var breakingNewsResponse : NewsResponse? = null

    private val _status = MutableLiveData<ApiStatus>()

    val countries = arrayOf("China","India","USA")
    val countriesId = arrayOf("cn","in","us")

    init {
        viewModelScope.launch {
            repository.saveCountry(Country(0,"USA","us"))
            breakingNewsPage=1
            getBreakingNews(repository.getCountry())
        }
    }

    private suspend fun safeBreakingNewsCall(countryCode: String){
        _breakingNews.postValue(Resource.Loading())
        try{
            if(hasInternetConnection()){
                val response = repository.getBreakingNews(countryCode,breakingNewsPage)
                _breakingNews.postValue(handleBreakingNewsResponse(response))
            }
            else{
                _breakingNews.postValue(Resource.Error("No Internet Connection"))
            }
        }
        catch (t : Throwable){
            when(t){
                is IOException -> {
                    _breakingNews.postValue(Resource.Error("Network Failure."))
                }
                else -> {
                    _breakingNews.postValue(Resource.Error("Conversion Error."))
                }
            }
        }
    }

    fun getBreakingNews(countryCode : String){
         viewModelScope.launch {
             safeBreakingNewsCall(countryCode)
         }
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {

        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                breakingNewsPage++
                if(breakingNewsPage==2){
                    breakingNewsResponse=resultResponse
                }
                else {
                    val newArticles = resultResponse.articles
                    (breakingNewsResponse!!.articles).addAll(newArticles)
                }

                return Resource.Success(breakingNewsResponse!!)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        repository.upsert(article)
    }

    fun deleteArticle(article: Article) = viewModelScope.launch {
        repository.delete(article)
    }

    fun getSavedNews() = repository.getSavedNews()

    private fun hasInternetConnection() : Boolean{
        val connectivityManager = getApplication<NewsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

            return when{
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else{
            connectivityManager.activeNetworkInfo?.run {
                return when(type){
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET ->  true
                    else -> false
                }
            }
        }
        return false
    }

    fun saveCountry(country: Country) {
        viewModelScope.launch {
            repository.saveCountry(country)
        }
    }
}