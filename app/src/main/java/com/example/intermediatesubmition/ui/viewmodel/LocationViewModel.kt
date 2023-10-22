package com.example.intermediatesubmition.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.intermediatesubmition.data.repository.StoryLocationRepository

class LocationViewModel(private val storyLocationRepository: StoryLocationRepository): ViewModel() {
    fun getStoryLocation() = storyLocationRepository.fetchStoriesLocation()
}