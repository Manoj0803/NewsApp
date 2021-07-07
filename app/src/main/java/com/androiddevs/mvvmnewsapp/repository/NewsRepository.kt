package com.androiddevs.mvvmnewsapp.repository

import android.app.DownloadManager
import com.androiddevs.mvvmnewsapp.api.RetrofitInstance
import com.androiddevs.mvvmnewsapp.db.ArticleDatabase
import com.androiddevs.mvvmnewsapp.models.Article
import com.androiddevs.mvvmnewsapp.models.NewsResponse
import retrofit2.Response

class NewsRepository(val db : ArticleDatabase) {

    suspend fun getBreakingNews(countryCode: String, pageNumber : Int): Response<NewsResponse> {
        return RetrofitInstance.api.getBreakingNews(countryCode,pageNumber)
    }

    suspend fun searchNews(searchQuery: String, pageNumber: Int) : Response<NewsResponse>
        =  RetrofitInstance.api.searchForNews(searchQuery,pageNumber)

    suspend fun upsert(article : Article) = db.getArticleDao().upsert(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun delete(article: Article) = db.getArticleDao().deleteArticle(article)

}