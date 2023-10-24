package com.example.intermediatesubmition.di

import android.content.Context
import com.example.intermediatesubmition.data.local.room.StoriesDatabase
import com.example.intermediatesubmition.data.remote.retrofit.ApiConfig
import com.example.intermediatesubmition.data.repository.StoryRepository
import com.example.intermediatesubmition.utils.Executor

object Injection {
    fun storyProvideRepository(context: Context): StoryRepository{
        val apiService = ApiConfig.getApiService()
        val database = StoriesDatabase.getInstance(context)
        val dao = database.storiesDao()
        val appExecutor = Executor()

        return StoryRepository.getInstance(apiService, dao, appExecutor)
    }
}