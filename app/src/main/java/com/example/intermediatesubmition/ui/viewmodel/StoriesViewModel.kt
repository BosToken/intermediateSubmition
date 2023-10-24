package com.example.intermediatesubmition.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.intermediatesubmition.data.repository.StoryRepository

class StoriesViewModel(private val storiesRepository: StoryRepository): ViewModel() {
    fun getStories(token: String) = storiesRepository.getStories(token)
}