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
import com.example.newsapp.Roomdatabase.NewsDatabase
import com.example.newsapp.Roomdatabase.NewsEntity
import kotlinx.coroutines.launch

class MainViewModel(context: Context): ViewModel() {

    val selectedTabForNavigationBar = mutableIntStateOf(0)
    val selectedTabForTabRow = mutableIntStateOf(0)

    val categories = listOf("Delhi", "India", "Sports", "Technology")
    val bottomNavigationCategories = listOf("Home", "Saved", "Settings")
    val bottomNavigationImages = listOf(Icons.Filled.Home, Icons.Filled.Favorite, Icons.Filled.Settings)

    private val tab0:List<Items> = listOf()
    private val tab1:List<Items> = listOf()
    private val tab2:List<Items> = listOf()
    private val tab3:List<Items> = listOf()
    private val saved:MutableList<Items> = mutableListOf()

    private val database = NewsDatabase.getDatabase(context)

    val listItemsHomeScreen: MutableState<List<Items>> = mutableStateOf<List<Items>>(tab0)

    fun changeTabs(tabNumber:Int){
        when(tabNumber){
            0 -> this.listItemsHomeScreen.value = tab0
            1 -> this.listItemsHomeScreen.value = tab1
            2 -> this.listItemsHomeScreen.value = tab2
            3 -> this.listItemsHomeScreen.value = tab3
        }
    }

    suspend fun addToDatabase(item: Items){
        database.newsDao().insertData(NewsEntity(0, item.img, item.headline, item.source, item.date))
    }

    suspend fun deleteFromDatabase(item: Items){
        database.newsDao().delete(item.headline)
    }

    fun homeButtonClicked(){
        this.listItemsHomeScreen.value = tab0
        this.selectedTabForTabRow.intValue = 0
    }

    fun savedButtonClicked(){
        viewModelScope.launch {
            val newsEntities: List<NewsEntity> = database.newsDao().getAll()
            saved.clear()
            for(i in newsEntities.indices){
                saved.add(
                    Items(
                    newsEntities[i].img,
                    newsEntities[i].headline,
                    newsEntities[i].source,
                    newsEntities[i].date
                    )
                )
            }
            listItemsHomeScreen.value = saved
        }
    }

}