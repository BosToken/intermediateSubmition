package com.example.intermediatesubmition.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.intermediatesubmition.data.api.response.StoryResponse
import com.example.intermediatesubmition.data.api.retrofit.ApiService
import com.example.intermediatesubmition.data.database.StoryDatabase
import com.example.intermediatesubmition.data.source.StoryPagingSource

class StoryRepository(private val storyDatabase: StoryDatabase, private val apiService: ApiService) {
    fun getStory(): LiveData<PagingData<StoryResponse>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService)
            }
        ).liveData
    }
}