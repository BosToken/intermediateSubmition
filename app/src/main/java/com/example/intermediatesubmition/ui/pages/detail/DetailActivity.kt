package com.example.intermediatesubmition.ui.pages.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.intermediatesubmition.data.remote.response.ResponseDetail
import com.example.intermediatesubmition.data.remote.retrofit.ApiConfig
import com.example.intermediatesubmition.data.local.prefrence.UserPreferences
import com.example.intermediatesubmition.data.local.prefrence.dataStore
import com.example.intermediatesubmition.ui.viewmodel.UserViewModel
import com.example.intermediatesubmition.databinding.ActivityDetailBinding
import com.example.intermediatesubmition.ui.factory.UserModelFactory
import com.example.intermediatesubmition.ui.pages.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val name = intent.getStringExtra("name")
        val photoUrl = intent.getStringExtra("photoUrl")
        val description = intent.getStringExtra("description")
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
                setDetailStory(photoUrl, name, description)
            }
        }

        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setDetailStory(photoUrl: String?, name: String?, description: String?){
        Glide.with(this).load(photoUrl).into(binding.imageView)
        binding.textView.text = name
        binding.textView2.text = description
    }

    private fun toLogin(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}