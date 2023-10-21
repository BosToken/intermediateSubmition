package com.example.intermediatesubmition.data.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.intermediatesubmition.data.api.response.StoryResponse
import com.example.intermediatesubmition.data.repository.StoryRepository

class StoryViewModel (storyRepository: StoryRepository) : ViewModel() {

    val story: LiveData<PagingData<StoryResponse>> =
        storyRepository.getStory().cachedIn(viewModelScope)
}