package com.example.intermediatesubmition.data.repository

import android.util.Log
import com.example.intermediatesubmition.data.remote.response.ListStoryItem
import com.example.intermediatesubmition.data.remote.response.StoryResponse
import com.example.intermediatesubmition.data.remote.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryLocationRepository private constructor(private val apiService: ApiService, private val token: String) {

    fun fetchStoriesLocation(): List<ListStoryItem>?{
        var responseBody: List<ListStoryItem>? = null
        Log.d("test123", token)
        apiService.getStoriesWithLocation("Bearer " + token).enqueue(object : Callback<StoryResponse>{
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                if (response.isSuccessful) {
                    responseBody = response.body()!!.listStory
                }
                else{
                    responseBody = null
                }
            }
            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
            }
        })

        return responseBody
    }

    companion object {
        @Volatile
        private var instance: StoryLocationRepository? = null
        fun getInstance(
            apiService: ApiService,
            token: String
        ): StoryLocationRepository =
            instance ?: synchronized(this) {
                instance ?: StoryLocationRepository(apiService, token)
            }.also { instance = it }
    }
}