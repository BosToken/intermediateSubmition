package com.example.intermediatesubmition.helper

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.intermediatesubmition.data.api.retrofit.ApiConfig
import com.example.intermediatesubmition.data.database.StoryDatabase
import com.example.intermediatesubmition.data.model.StoryViewModel
import com.example.intermediatesubmition.data.repository.StoryRepository

class StoryModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StoryViewModel(Injection.provideRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    object Injection {
        fun provideRepository(context: Context): StoryRepository {
            val database = StoryDatabase.getDatabase(context)
            val apiService = ApiConfig.getApiService()
            return StoryRepository(database, apiService)
        }
    }
}