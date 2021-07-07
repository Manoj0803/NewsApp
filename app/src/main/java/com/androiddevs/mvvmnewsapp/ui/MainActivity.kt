package com.androiddevs.mvvmnewsapp.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.databinding.ActivityMainBinding
import com.androiddevs.mvvmnewsapp.db.ArticleDatabase
import com.androiddevs.mvvmnewsapp.repository.NewsRepository
import com.androiddevs.mvvmnewsapp.viewmodel.NewsViewModel
import com.androiddevs.mvvmnewsapp.viewmodel.NewsViewModelFactory


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val newsRepository = NewsRepository(ArticleDatabase.getDatabase(this))
        val factory = NewsViewModelFactory(newsRepository)
        val viewModel = ViewModelProvider(this,factory).get(NewsViewModel::class.java)

        binding.bottomNavigationView.setupWithNavController(Navigation.findNavController(this,R.id.news_nav_host_fragment))
    }
}
