package com.androiddevs.mvvmnewsapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.androiddevs.mvvmnewsapp.repository.NewsRepository

class BreakingNewsViewModelFactory(
    val application: Application,
    private val repository: NewsRepository
    ) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewsViewModel(application,repository) as T
    }
}