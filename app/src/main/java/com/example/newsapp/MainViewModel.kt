package com.example.newsapp

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.roomdatabase.NewsDatabase
import com.example.newsapp.roomdatabase.NewsEntity
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainViewModel(context: Context) : ViewModel() {

    val selectedTabForNavigationBar = mutableIntStateOf(0)
    val selectedTabForTabRow = mutableIntStateOf(0)

    val bottomNavigationCategories = listOf("Home", "Saved", "Settings")
    val bottomNavigationImages =
        listOf(Icons.Filled.Home, Icons.Filled.Favorite, Icons.Filled.Settings)

    var city:String = ""
    var country = ""

    val categories = mutableListOf("New Delhi", "International", "Sports", "Technology")

    public val tab0:MutableList<Items> = mutableListOf()
    public val tab1:MutableList<Items> = mutableListOf()
    public val tab2:MutableList<Items> = mutableListOf()
    public val tab3:MutableList<Items> = mutableListOf()

    private val saved: MutableList<Items> = mutableListOf()

    private val database = NewsDatabase.getDatabase(context)

    val listItemsHomeScreen: MutableState<List<Items>> = mutableStateOf<List<Items>>(tab0)

    fun changeTabs(tabNumber: Int) {
        when (tabNumber) {
            0 -> this.listItemsHomeScreen.value = tab0
            1 -> this.listItemsHomeScreen.value = tab1
            2 -> this.listItemsHomeScreen.value = tab2
            3 -> this.listItemsHomeScreen.value = tab3
        }
    }

    suspend fun addToDatabase(item: Items) {
        database.newsDao().insertData(
            NewsEntity(
                0,
                item.img,
                item.headline,
                item.source,
                item.date,
                item.content
            )
        )
    }

    suspend fun deleteFromDatabase(item: Items) {
        database.newsDao().delete(item.headline)
    }

    fun homeButtonClicked() {
        this.listItemsHomeScreen.value = tab0
        this.selectedTabForTabRow.intValue = 0
    }

    fun savedButtonClicked() {
        viewModelScope.launch {
            val newsEntities: List<NewsEntity> = database.newsDao().getAll()
            saved.clear()
            for (i in newsEntities.indices) {
                saved.add(
                    Items(
                        newsEntities[i].img,
                        newsEntities[i].headline,
                        newsEntities[i].source,
                        newsEntities[i].date,
                        newsEntities[i].content
                    )
                )
            }
            listItemsHomeScreen.value = saved
        }
    }

    fun getNews(query: String, fromDate: String, sortBy: String, apiKey: String): Call<NewsResponse> {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(NewsService::class.java)
        return service.getNews(query, fromDate, sortBy, "en", apiKey)
    }

}