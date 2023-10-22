package com.example.intermediatesubmition.helper

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.intermediatesubmition.data.repository.StoryLocationRepository
import com.example.intermediatesubmition.di.Injection
import com.example.intermediatesubmition.ui.viewmodel.LocationViewModel

class StoryLocationFactory private constructor(private val storyLocationRepository: StoryLocationRepository): ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocationViewModel::class.java)) {
            return LocationViewModel(storyLocationRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: StoryLocationFactory? = null
        fun getInstance(token: String): StoryLocationFactory =
            instance ?: synchronized(this) {
                instance ?: StoryLocationFactory(Injection.locationProvideRepository(token))
            }.also { instance = it }
    }

}