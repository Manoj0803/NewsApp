package com.androiddevs.mvvmnewsapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.mvvmnewsapp.models.Article
import com.androiddevs.mvvmnewsapp.models.NewsResponse
import com.androiddevs.mvvmnewsapp.repository.NewsRepository
import com.androiddevs.mvvmnewsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

enum class ApiStatus { LOADING, ERROR, DONE }


class NewsViewModel(private val repository : NewsRepository) : ViewModel() {

    private val _breakingNews = MutableLiveData<Resource<NewsResponse>>()
    val breakingNews : LiveData<Resource<NewsResponse>>
        get() = _breakingNews

    var breakingNewsPage : Int = 1
    private var breakingNewsResponse : NewsResponse? = null

    private val _searchNews = MutableLiveData<Resource<NewsResponse>>()
    val searchNews : LiveData<Resource<NewsResponse>>
        get() = _searchNews

    var searchNewsPage : Int = 1
    private var searchNewsResponse : NewsResponse? = null

    private val _status = MutableLiveData<ApiStatus>()
    val status : LiveData<ApiStatus>
        get() = _status

//    private val _navigate = MutableLiveData<Boolean>()
//    val navigate : LiveData<Boolean>
//        get() = _navigate

    init {
        getBreakingNews("in")
    }



    fun getBreakingNews(countryCode : String){
         viewModelScope.launch {
             try{
                 _status.value=ApiStatus.LOADING
                 _breakingNews.postValue(Resource.Loading())
                 val response = repository.getBreakingNews(countryCode,breakingNewsPage)
                 _breakingNews.postValue(handleBreakingNewsResponse(response))
                 _status.value=ApiStatus.DONE
             }
             catch (e : Exception)
             {
                 _status.value=ApiStatus.ERROR
             }
         }
    }

    fun searchNews(searchQuery : String){
        viewModelScope.launch {
            try{
                _status.value=ApiStatus.LOADING
                _searchNews.postValue(Resource.Loading())
                val response = repository.searchNews(searchQuery,searchNewsPage)
                _searchNews.postValue(handleSearchNewsResponse(response))
                _status.value=ApiStatus.DONE
            }
            catch (e : Exception){
                _status.value=ApiStatus.ERROR
            }
        }
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->

                searchNewsPage++
                if(searchNewsResponse==null){
                    searchNewsResponse=resultResponse
                } else{
//                      val oldArticles = searchNewsResponse!!.articles
                    val newArticles = resultResponse.articles
                    (searchNewsResponse!!.articles).addAll(newArticles)
                }
                return Resource.Success(searchNewsResponse!!)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {

        if(response.isSuccessful){
            response.body()?.let { resultResponse ->

                breakingNewsPage++
                if(breakingNewsResponse==null){
                    breakingNewsResponse=resultResponse
                }
                else {
//                    val oldArticles = breakingNewsResponse!!.articles
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
}