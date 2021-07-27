package com.androiddevs.mvvmnewsapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.setupWithNavController(Navigation.findNavController(this,R.id.news_nav_host_fragment))
        setupActionBarWithNavController(findNavController(R.id.news_nav_host_fragment))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(
            R.menu.location_menu,
            menu
        )
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.news_nav_host_fragment)
        return navController.navigateUp() or super.onSupportNavigateUp()
    }

}
