package com.example.intermediatesubmition.ui.pages.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.intermediatesubmition.R
import com.example.intermediatesubmition.data.remote.response.ListStoryItem
import com.example.intermediatesubmition.data.remote.response.StoryResponse
import com.example.intermediatesubmition.data.remote.retrofit.ApiConfig
import com.example.intermediatesubmition.data.database.UserPreferences
import com.example.intermediatesubmition.data.database.dataStore
import com.example.intermediatesubmition.ui.viewmodel.UserViewModel
import com.example.intermediatesubmition.databinding.ActivityMainBinding
import com.example.intermediatesubmition.helper.UserModelFactory
import com.example.intermediatesubmition.ui.pages.login.LoginActivity
import com.example.intermediatesubmition.ui.pages.map.MapsActivity
import com.example.intermediatesubmition.ui.pages.store.StoreActivity
import com.example.intermediatesubmition.ui.pages.testing.TestingActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException

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
        try {
            val client = ApiConfig.getApiService().getStories("Bearer " + token)
            client.enqueue(object : Callback<StoryResponse> {
                override fun onResponse(
                    call: Call<StoryResponse>,
                    response: retrofit2.Response<StoryResponse>
                ) {
                    if (response.isSuccessful){
                        val responseBody = response.body()!!.listStory
                        setStory(responseBody)
                    }
                    else{
                        toLogin()
                    }
                }

                override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }
        catch (e: HttpException){
            toLogin()
        }
    }

    private fun setStory(storyList : List<ListStoryItem>){
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
//        val intent = Intent(this, TestingActivity::class.java)
        startActivity(intent)
    }

    private fun toLogin(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}