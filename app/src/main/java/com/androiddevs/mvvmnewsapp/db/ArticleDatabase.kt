package com.androiddevs.mvvmnewsapp.db

import android.content.Context
import androidx.room.*
import com.androiddevs.mvvmnewsapp.models.Article

@Database(
    entities = [Article::class] ,
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase(){

    abstract fun getArticleDao() : ArticleDao

    companion object{
        @Volatile
        private var INSTANCE : ArticleDatabase? = null

            fun getDatabase(context : Context): ArticleDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ArticleDatabase::class.java,"article_database"
                ).build()
                INSTANCE=instance
                return instance
            }
        }
    }
}