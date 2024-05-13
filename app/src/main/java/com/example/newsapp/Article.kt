package com.example.newsapp

import com.google.gson.annotations.SerializedName

data class Article(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String?,
    @SerializedName("url") val url: String,
    @SerializedName("content") val content: String,
    @SerializedName("publishedAt" ) var publishedAt : String? = null,
    @SerializedName("Source") val source: Source
)


data class NewsResponse (

    @SerializedName("status"       ) var status       : String?             = null,
    @SerializedName("totalResults" ) var totalResults : Int?                = null,
    @SerializedName("articles"     ) var articles     : ArrayList<Article> = arrayListOf()

)

data class Source (

    @SerializedName("id"   ) var id   : String? = null,
    @SerializedName("name" ) var name : String? = null

)
