package com.example.newsapp.Roomdatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(orientation: NewsEntity)

    @Delete
    suspend fun deleteContact(weather: NewsEntity)

    @Query("DELETE FROM news")
    suspend fun deleteAll()

    @Query("SELECT * FROM news")
    suspend fun getAll(): List<NewsEntity>
}