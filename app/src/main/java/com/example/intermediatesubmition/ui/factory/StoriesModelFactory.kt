package com.example.intermediatesubmition.ui.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.intermediatesubmition.data.repository.StoryRepository
import com.example.intermediatesubmition.di.Injection
import com.example.intermediatesubmition.ui.viewmodel.StoriesViewModel

class StoriesModelFactory private constructor(private val storiesRepository: StoryRepository): ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(StoriesViewModel::class.java)){
            return StoriesViewModel(storiesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class: "+modelClass.name)
    }

    companion object{
        @Volatile
        private var instance: StoriesModelFactory? = null
        fun getInstance(context: Context): StoriesModelFactory =
            instance ?: synchronized(this) {
                instance ?: StoriesModelFactory(Injection.storyProvideRepository(context))
            }.also { instance = it }
    }
}