package com.example.newsapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {
    @GET("everything")
    fun getNews(
        @Query("q") query: String,
        @Query("from") fromDate: String,
        @Query("sortBy") sortBy: String,
        @Query("language") lang: String="en",
        @Query("apiKey") apiKey: String
    ): Call<NewsResponse>
}
