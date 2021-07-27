package com.androiddevs.mvvmnewsapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.androiddevs.mvvmnewsapp.models.Article
import com.androiddevs.mvvmnewsapp.models.Country

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article : Article) : Long

    @Query("select * from articles")
    fun getAllArticles() : LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article : Article)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountry(country : Country) : Long

    @Query("select countryId from country")
    suspend fun getCountry() : String

}