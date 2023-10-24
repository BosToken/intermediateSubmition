package com.example.intermediatesubmition.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.intermediatesubmition.data.local.entity.StoriesEntity
import com.example.intermediatesubmition.data.local.room.StoriesDAO
import com.example.intermediatesubmition.data.remote.retrofit.ApiService
import com.example.intermediatesubmition.utils.Executor
import com.example.intermediatesubmition.data.Result
import com.example.intermediatesubmition.data.remote.response.StoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val storiesDAO: StoriesDAO,
    private val appExecutor: Executor
){
    private val result = MediatorLiveData<Result<List<StoriesEntity>>>()

    fun getStories(token: String): LiveData<Result<List<StoriesEntity>>>{
        result.value = Result.Loading
        val client = apiService.getStories("Bearer " + token)
        client.enqueue(object : Callback<StoryResponse>{
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                if (response.isSuccessful){
                    val responseBody = response.body()?.listStory
                    val storiesList = ArrayList<StoriesEntity>()
                    appExecutor.diskIO.execute{
                        responseBody?.forEach { story ->
                            val stories = StoriesEntity(
                                id = story.id,
                                name = story.name,
                                description = story.description,
                                createdAt = story.createdAt,
                                photoUrl = story.photoUrl,
                            )
                            storiesList.add(stories)
                        }
                        storiesDAO.deleteAll()
                        storiesDAO.insertStories(storiesList)
                    }
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }
        })
        val localData = storiesDAO.getStories()
        result.addSource(localData){ newData : List<StoriesEntity> ->
            result.value = Result.Success(newData)
        }

        return result
    }

    companion object{
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            apiService: ApiService,
            storiesDAO: StoriesDAO,
            appExecutor: Executor
        ): StoryRepository =
            instance ?: synchronized(this){
                instance ?: StoryRepository(apiService, storiesDAO, appExecutor)
            }.also {
                instance = it
            }
    }
}