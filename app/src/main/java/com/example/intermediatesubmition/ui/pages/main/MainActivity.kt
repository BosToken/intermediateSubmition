package com.example.intermediatesubmition.ui.pages.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.intermediatesubmition.R
import com.example.intermediatesubmition.data.Result
import com.example.intermediatesubmition.data.local.entity.StoriesEntity
import com.example.intermediatesubmition.data.local.prefrence.UserPreferences
import com.example.intermediatesubmition.data.local.prefrence.dataStore
import com.example.intermediatesubmition.ui.viewmodel.UserViewModel
import com.example.intermediatesubmition.databinding.ActivityMainBinding
import com.example.intermediatesubmition.ui.factory.StoriesModelFactory
import com.example.intermediatesubmition.ui.factory.UserModelFactory
import com.example.intermediatesubmition.ui.pages.login.LoginActivity
import com.example.intermediatesubmition.ui.pages.map.MapsActivity
import com.example.intermediatesubmition.ui.pages.store.StoreActivity
import com.example.intermediatesubmition.ui.viewmodel.StoriesViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val pref = UserPreferences.getInstance(application.dataStore)
        val mainViewModel = ViewModelProvider(this, UserModelFactory(pref)).get(
            UserViewModel::class.java
        )

        mainViewModel.getToken().observe(this){
                token ->
            if (token == "null"){
                toLogin()
            }
            else{
                getStoryApi(token)
            }
        }

        binding.fab.setOnClickListener{
            val intent = Intent(this, StoreActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        binding.toolbar.inflateMenu(R.menu.main_menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logoutMenu -> {
                logout()
                return true
            }
            R.id.mapsMenu -> {
                maps()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getStoryApi(token: String){
        val favoriteViewModel = obtainViewModel(this)


        favoriteViewModel.getStories(token).observe(this){storiesList ->
            when(storiesList){
                is Result.Success -> {
                    setStory(storiesList.data)
                }
                is Result.Error -> {
                    showToast(storiesList.error)
                }
                else -> {
                }
            }
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): StoriesViewModel {
        val factory = StoriesModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(StoriesViewModel::class.java)
    }

    private fun setStory(storyList : List<StoriesEntity>){
        val adapter = StoryAdapter(storyList)
        binding.rvItem.layoutManager = LinearLayoutManager(binding.rvItem.context)
        binding.rvItem.adapter = adapter
    }

    private fun logout(){
        val pref = UserPreferences.getInstance(application.dataStore)
        val mainViewModel = ViewModelProvider(this, UserModelFactory(pref)).get(UserViewModel::class.java)
        mainViewModel.saveToken("null")
        toLogin()
    }

    private fun maps(){
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }

    private fun toLogin(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun showToast(response: String): Toast {
        return Toast.makeText(this, response, Toast.LENGTH_SHORT)
    }
}