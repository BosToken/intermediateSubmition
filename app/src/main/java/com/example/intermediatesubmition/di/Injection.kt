package com.example.intermediatesubmition.di

import android.content.Context
import com.example.intermediatesubmition.data.database.UserPreferences
import com.example.intermediatesubmition.data.database.dataStore
import com.example.intermediatesubmition.data.remote.retrofit.ApiConfig
import com.example.intermediatesubmition.data.repository.StoryLocationRepository
import com.example.intermediatesubmition.ui.viewmodel.UserViewModel
import com.example.intermediatesubmition.utils.Executor

object Injection {
    fun locationProvideRepository(token: String): StoryLocationRepository{
        val apiService = ApiConfig.getApiService()
        return StoryLocationRepository.getInstance(apiService, token)
    }
}