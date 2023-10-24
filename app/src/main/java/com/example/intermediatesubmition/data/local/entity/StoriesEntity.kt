package com.example.intermediatesubmition.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stories")
data class StoriesEntity (
    @field:PrimaryKey
    @field:ColumnInfo(name = "id")
    var id: String,

    @field:ColumnInfo(name = "name")
    var name: String? = null,

    @field:ColumnInfo(name = "description")
    var description: String? = null,

    @field:ColumnInfo(name = "createdAt")
    var createdAt: String? = null,

    @field:ColumnInfo(name = "photoUrl")
    var photoUrl: String? = null,

)