package com.androiddevs.mvvmnewsapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.androiddevs.mvvmnewsapp.repository.NewsRepository

class SearchNewsViewModelFactory(
    val application: Application,
    private val repository: NewsRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchNewsViewModel(application,repository) as T
    }
}