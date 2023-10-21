package com.example.intermediatesubmition.data.source


import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.intermediatesubmition.data.api.response.StoryResponse
import com.example.intermediatesubmition.data.api.retrofit.ApiService

class StoryPagingSource(private val apiService: ApiService): PagingSource<Int, StoryResponse>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, StoryResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryResponse> {
        TODO("Not yet implemented")
    }

}