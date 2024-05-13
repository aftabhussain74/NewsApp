package com.example.newsapp.Roomdatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news")
data class NewsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val img: Int,
    val headline: String,
    val source: String,
    val date: String
)