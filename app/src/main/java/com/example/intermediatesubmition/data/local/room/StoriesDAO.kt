package com.example.intermediatesubmition.data.local.room

import androidx.lifecycle.LiveData
import com.example.intermediatesubmition.data.local.entity.StoriesEntity
import androidx.room.*

@Dao
interface StoriesDAO {
    @Query("SELECT * FROM stories")
    fun getStories(): LiveData<List<StoriesEntity>>

//    @Query("SELECT * FROM stories WHERE lat IS NOT NULL")
//    fun getStoriesWhereLocation(): LiveData<List<StoriesEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertStories(stories: List<StoriesEntity>)

    @Update
    fun updateStories(stories: StoriesEntity)

    @Query("DELETE FROM stories WHERE id IS NOT NULL")
    fun deleteAll()
}