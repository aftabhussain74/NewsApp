package com.example.newsapp.roomdatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(newsItem: NewsEntity)

    @Query("DELETE FROM news")
    suspend fun deleteAll()

    @Query("SELECT * FROM news ORDER BY id DESC")
    suspend fun getAll(): List<NewsEntity>

    @Query("DELETE FROM news where headline==:headline")
    suspend fun delete(headline:String)
}