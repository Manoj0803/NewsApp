package com.androiddevs.mvvmnewsapp.ui

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(
//            R.menu.location_menu,
//            menu
//        )
//        return true
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//
//        if(item.itemId==R.id.location_menu){
//            changeLocation()
//        }
//
//        return super.onOptionsItemSelected(item)
//    }

//    private fun changeLocation() {
//
//        Toast.makeText(
//            this,
//            "Clicked",
//            Toast.LENGTH_LONG
//        ).show()
//
//        val options = arrayOf("1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20")
//        val builder = AlertDialog.Builder(this)
//
//        builder.setTitle("Location")
//            .setItems(options) { _, i ->
//                Toast.makeText(this, "Item Click : ${options[i]}", Toast.LENGTH_SHORT).show()
//            }
//        builder.create().show()
//    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.news_nav_host_fragment)
        return navController.navigateUp() or super.onSupportNavigateUp()
    }

}
